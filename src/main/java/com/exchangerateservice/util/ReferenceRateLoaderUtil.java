package com.exchangerateservice.util;

import com.exchangerateservice.service.dataloader.ReferenceRateDataLoaderService;
import com.exchangerateservice.service.exchnage.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReferenceRateLoaderUtil {

    private Logger log = LoggerFactory.getLogger(ReferenceRateLoaderUtil.class);

    @Autowired
    private ReferenceRateDataLoaderService exchangeRateDataLoaderService;

    @Autowired
    private ExchangeRateService exchangeRateService;


    @Scheduled(fixedDelay = 60*60*1000)
    private void loadExchangeRateFromECB(){
      Map referenceRateFromECB = exchangeRateDataLoaderService.updateReferenceRateFromECB();
      if(referenceRateFromECB != null){
          log.info("Updating new exchange rate data");
          exchangeRateService.updateReferenceRateData(referenceRateFromECB);
          log.info("Successfully Updated new exchange rate data");
          log.info("adding all supported currency");
          exchangeRateService.loadSupportedCurrency();
          log.info("Successfully added all supported currency");
      }
    }

}
