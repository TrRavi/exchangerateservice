package com.exchangerateservice.controller;

import com.exchangerateservice.dto.ChartDTO;
import com.exchangerateservice.dto.ConvertedCurrencyDTO;
import com.exchangerateservice.dto.ReferenceRateDTO;
import com.exchangerateservice.service.exchnage.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReferenceRateController {
    private static final Logger log = LoggerFactory.getLogger(ReferenceRateController.class);

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping("/referencerate")
    public ResponseEntity<ReferenceRateDTO> getReferenceRate(@RequestParam("baseCurrency") String baseCurrency,
                                                             @RequestParam("toCurrency") String toCurrency){
        ReferenceRateDTO referenceRateDTO = exchangeRateService.getReferenceRate(baseCurrency,toCurrency);
        return ResponseEntity.ok(referenceRateDTO);
    }

    @GetMapping("/supportedCurrency")
    public ResponseEntity<Map<String,Long>> getAllSupportedCurrencyAndRequestedCount(){
        return ResponseEntity.ok(exchangeRateService.getAllSupportedCurrencyAndRequestedCount());
    }

    @GetMapping("/convertCurrency")
    public ResponseEntity<ConvertedCurrencyDTO> convertCurrency(@RequestParam("baseCurrency") String baseCurrency,
                                                                @RequestParam("toCurrency") String toCurrency,
                                                                @RequestParam("baseCurrencyAmount") Double baseCurrencyAmount){
        log.info("here it is in controller");
        return ResponseEntity.ok(exchangeRateService.convertCurrency(baseCurrency,toCurrency,baseCurrencyAmount));
    }

    @GetMapping("/chart")
    public ResponseEntity<ChartDTO> getChartForCurrency(@RequestParam("currency") String currency){
        return ResponseEntity.ok(exchangeRateService.getChartForCurrency(currency));
    }
}
