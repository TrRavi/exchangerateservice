package com.exchangerateservice.service.dataloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class ReferenceRateDataLoaderServiceImpl implements ReferenceRateDataLoaderService {

    Logger log = LoggerFactory.getLogger(ReferenceRateDataLoaderServiceImpl.class);

    @Value("${exchange.rate.endpoint}")
    private static String ecbURL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml";

    @Override
    public Map updateReferenceRateFromECB(){
        try{
            HttpHeaders httpHeaders =new HttpHeaders();
            httpHeaders.add("Accept","application/xml");
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> exchangeRateResponse = restTemplate.exchange(ecbURL, HttpMethod.GET, httpEntity, Map.class);
            if(exchangeRateResponse.getStatusCodeValue() == 200){
                log.info("Successfully received the Reference rate data from ECB: {}",ecbURL);
                return exchangeRateResponse.getBody();
            }else {
                log.error("Could not get Reference Data from EBC: {} \nstatusCode is: {}",ecbURL,exchangeRateResponse.getStatusCodeValue());
            }
        }catch (Exception e){
            log.error("Could not get Reference Data from EBC: {}, \nerror is :- {}",ecbURL,e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
