package com.exchangerateservice.config;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

@Aspect
@Component
public class CurrencyRequestedCount {

    private static final Logger log = LoggerFactory.getLogger(CurrencyRequestedCount.class);

    @Autowired
    SupportedCurrencyAndRequestedCount supportedCurrencyAndRequestedCount;



    @AfterReturning("execution(* com.exchangerateservice.controller.ReferenceRateController.*(..))")
    public void countRequestCurrency(){
        Map<String, Long> supportCurrencyAndUsesCount = supportedCurrencyAndRequestedCount.getSupportCurrencyAndUsesCount();
        // request attribute
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("{}, :: finished",request.getRequestURL());
        Map paramMap = request.getParameterMap();
        Iterator itr = paramMap.keySet().iterator();
        while (itr.hasNext()) {
            String paramName = (String) itr.next();
            String paramValue = request.getParameter(paramName);
            if(supportCurrencyAndUsesCount.containsKey(paramValue)){
                Long currencyRequestedCount = supportCurrencyAndUsesCount.get(paramValue);
                currencyRequestedCount++;
                supportCurrencyAndUsesCount.put(paramValue,currencyRequestedCount);
            }
        }
    }

}
