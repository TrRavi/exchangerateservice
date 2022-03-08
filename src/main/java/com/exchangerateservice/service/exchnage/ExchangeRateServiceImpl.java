package com.exchangerateservice.service.exchnage;

import com.exchangerateservice.Constant;
import com.exchangerateservice.config.SupportedCurrencyAndRequestedCount;
import com.exchangerateservice.dto.ChartDTO;
import com.exchangerateservice.dto.ConvertedCurrencyDTO;
import com.exchangerateservice.dto.ReferenceRateDTO;
import com.exchangerateservice.entity.CurrencyReferenceRate;
import com.exchangerateservice.exception.CurrencyNotSupported;
import com.exchangerateservice.exception.ThirdPartyServerException;
import com.exchangerateservice.repository.CurrencyReferenceRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final Logger log = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);

    @Autowired
    private CurrencyReferenceRateRepository currencyExchangeRateRepository;

    @Autowired
    private SupportedCurrencyAndRequestedCount supportedCurrencyAndRequestedCount;

    @Value("${exchange.rate.chart.enpoint}")
    private String chartURL;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat decimalFormat = new DecimalFormat("#.#####    ");


    @Override
    public ReferenceRateDTO getReferenceRate(String baseCurrency, String currency) {
        baseCurrency = baseCurrency.toUpperCase();
        currency = currency.toUpperCase();

        //if base Currency is EUR, return the database row
        if (baseCurrency.equals(Constant.BASE_CURRENCY)) {
            CurrencyReferenceRate currencyReferenceRate = currencyExchangeRateRepository.findByCurrencyCode(currency);
            if (currencyReferenceRate == null)
                throw new CurrencyNotSupported(currency);

            return new ReferenceRateDTO(Constant.BASE_CURRENCY, currencyReferenceRate.getCurrencyCode(), currencyReferenceRate.getExchangeRate(), currencyReferenceRate.getDate());
        }

        //if base Currency is not EUR, but other one is in EUR
        if (currency.equals(Constant.BASE_CURRENCY)) {
            CurrencyReferenceRate currencyReferenceRate = currencyExchangeRateRepository.findByCurrencyCode(baseCurrency);
            if (currencyReferenceRate == null)
                throw new CurrencyNotSupported(baseCurrency);

            Double eurRefRate = currencyReferenceRate.getExchangeRate();//this rate is in respect of EUR
            Double refRate = roundNumber(1 / eurRefRate);
            return new ReferenceRateDTO(currencyReferenceRate.getCurrencyCode(), Constant.BASE_CURRENCY, refRate, currencyReferenceRate.getDate());
        }

        //if both currency & baseCurrency are different
        return calculateReferenceRateBetweenCurrencyPair(baseCurrency, currency);
    }


    @Override
    public Map<String, Long> getAllSupportedCurrencyAndRequestedCount() {
        return supportedCurrencyAndRequestedCount.getSupportCurrencyAndUsesCount();
    }

    @Override
    public ConvertedCurrencyDTO convertCurrency(String baseCurrency, String toCurrency, Double baseCurrencyAmount) {
        ReferenceRateDTO referenceRateDTO = getReferenceRate(baseCurrency, toCurrency);
        Double convertedAmount = referenceRateDTO.getExchangeRate() * baseCurrencyAmount;
        return new ConvertedCurrencyDTO(baseCurrency, toCurrency, baseCurrencyAmount, roundNumber(convertedAmount));
    }

    @Override
    public ChartDTO getChartForCurrency(String currency) {
        Map<String, Long> supportCurrencyAndUsesCount = supportedCurrencyAndRequestedCount.getSupportCurrencyAndUsesCount();
        if (!supportCurrencyAndUsesCount.containsKey(currency.toUpperCase()))
            throw new CurrencyNotSupported(currency);

        List<Integer> nums = new ArrayList<>();
        currency = currency.toLowerCase();
        chartURL = chartURL.replace("currencyCode", currency);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", "application/xml");
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> chartResponse = restTemplate.exchange(chartURL, HttpMethod.GET, httpEntity, Map.class);
            log.info("Successfully received the chart ECB: {}", chartURL);
            Map<String, Object> channel = (Map<String, Object>) chartResponse.getBody().get("channel");
            Map<String, Object> items = (Map<String, Object>) channel.get("items");
            Map<String, Object> seq = (Map<String, Object>) items.get("Seq");
            List<Map<String, String>> links = (List<Map<String, String>>) seq.get("li");
            List<String> chartLinkList = links.stream().map(l -> l.get("resource")).collect(Collectors.toList());
            return new ChartDTO(Constant.BASE_CURRENCY, currency, chartLinkList);
        } catch (HttpStatusCodeException e) {
            log.error("Could not get chart Data from EBC: {} ", chartURL);
            throw new ThirdPartyServerException(e.getStatusText(), e.getStatusCode());
        }
    }


    private ReferenceRateDTO calculateReferenceRateBetweenCurrencyPair(String baseCurrency, String currency) {
        CurrencyReferenceRate baseCurrencyReferenceRate = currencyExchangeRateRepository.findByCurrencyCode(baseCurrency);
        if (baseCurrencyReferenceRate == null)
            throw new CurrencyNotSupported(baseCurrency);

        CurrencyReferenceRate currencyReferenceRate = currencyExchangeRateRepository.findByCurrencyCode(currency);
        if (currencyReferenceRate == null)
            throw new CurrencyNotSupported(currency);


        Double refRate = currencyReferenceRate.getExchangeRate() / baseCurrencyReferenceRate.getExchangeRate();
        Double roundedRefRate = roundNumber(refRate);
        return new ReferenceRateDTO(baseCurrency, currency, roundedRefRate, currencyReferenceRate.getDate());
    }


    @Override
    public void updateReferenceRateData(Map exchangeRateFromECB) {
        //currently, saving most updated data set
        try {
            Map<String, Object> layer_1 = (Map<String, Object>) exchangeRateFromECB.get("Cube");
            List<Map<String, Object>> layer_2 = (List<Map<String, Object>>) layer_1.get("Cube");
            Map<String, Object> timeCube = layer_2.get(0);
            Date date = null;
            try {
                date = simpleDateFormat.parse((String) timeCube.get("time"));
            } catch (ParseException e) {
                log.error("error while parsing the date:- {}", e.getMessage());
            }
            List<Map<String, Object>> currencyReferenceRateRateCube = (List<Map<String, Object>>) timeCube.get("Cube");
            Date finalDate = date;
            List<CurrencyReferenceRate> currencyExchangeRateList = currencyReferenceRateRateCube.stream().map(cube -> {
                String currency = (String) cube.get("currency");
                Double rate = Double.parseDouble((String) cube.get("rate"));
                return new CurrencyReferenceRate(currency, finalDate, rate);
            }).collect(Collectors.toList());
            //delete all previous rates before inserting fresh
            currencyExchangeRateRepository.deleteAll();
            currencyExchangeRateRepository.saveAll(currencyExchangeRateList);
        } catch (Exception e) {
            log.error("error while parsing and saving into Database {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void loadSupportedCurrency() {
        List<CurrencyReferenceRate> currencyReferenceRateList = currencyExchangeRateRepository.findAll();
        Map<String, Long> supportCurrencyAndUsesCountMap = supportedCurrencyAndRequestedCount.getSupportCurrencyAndUsesCount();
        if (supportCurrencyAndUsesCountMap == null) {
            supportCurrencyAndUsesCountMap = new HashMap<>();
        }
        for (CurrencyReferenceRate rate : currencyReferenceRateList) {
            if (!supportCurrencyAndUsesCountMap.containsKey(rate.getCurrencyCode())) {
                supportCurrencyAndUsesCountMap.put(rate.getCurrencyCode(), 0L);
            }
        }
        supportCurrencyAndUsesCountMap.put(Constant.BASE_CURRENCY,0L);
        supportedCurrencyAndRequestedCount.setSupportCurrencyAndUsesCount(supportCurrencyAndUsesCountMap);
    }

    /*
    will round the number to 3 decimal points
     */
    private static Double roundNumber(Double number) {
        return Math.round(number * 1000.0) / 1000.0;
    }


    /*@Override
    public void updateExchangeRateData(Map exchangeRateFromECB) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        try {
           Map<String,Object> layer_1 = (Map<String, Object>) exchangeRateFromECB.get("Cube");
           List<Map<String,Object>> layer_2 = (List<Map<String,Object>>) layer_1.get("Cube");
           List<CurrencyExchangeRate> currencyExchangeRateList =   layer_2.stream().flatMap((Map<String, Object> timeCube) ->{
               Date date = null;
               try {
                   date = simpleDateFormat.parse((String) timeCube.get("time"));
               } catch (ParseException e) {
                   log.error("error while parsing the date:- {}",e.getMessage());
               }
               List<Map<String,Object>> childCube = (List<Map<String,Object>>)timeCube.get("Cube");
               Date finalDate = date;
               return  childCube.stream().map(cube ->{
                   String currency  = (String) cube.get("currency");
                   Double rate = Double.parseDouble((String) cube.get("rate"));
                   return new CurrencyExchangeRate(currency, finalDate,rate);
               });
           }).collect(Collectors.toList());
           currencyExchangeRateRepository.saveAll(currencyExchangeRateList);
       }catch (Exception e){
           log.error("error while parsing and saving into Database {}",e.getMessage());
           e.printStackTrace();
       }
    }*/


}
