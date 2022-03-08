package com.exchangerateservice.config;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This file will be used to store unique Currency Code which are supported, and a counter for requested.
 * Key => currency code
 * value => an Integer (uses count, how many times currency was requested)
 *
 * And All currency code will be added at Application start-up.
 */
@Component
public class SupportedCurrencyAndRequestedCount {

    private Map<String,Long> supportCurrencyAndUsesCount;

    public Map<String, Long> getSupportCurrencyAndUsesCount() {
        return supportCurrencyAndUsesCount;
    }

    public void setSupportCurrencyAndUsesCount(Map<String, Long> supportCurrencyAndUsesCount) {
        this.supportCurrencyAndUsesCount = supportCurrencyAndUsesCount;
    }
}
