package com.exchangerateservice.service.exchnage;

import com.exchangerateservice.dto.ChartDTO;
import com.exchangerateservice.dto.ConvertedCurrencyDTO;
import com.exchangerateservice.dto.ReferenceRateDTO;

import java.util.List;
import java.util.Map;

public interface ExchangeRateService {
    void updateReferenceRateData(Map exchangeRateFromECB);

    ReferenceRateDTO getReferenceRate(String baseCurrency, String toCurrency);

    void loadSupportedCurrency();

    Map<String,Long> getAllSupportedCurrencyAndRequestedCount();

    ChartDTO getChartForCurrency(String currency);

    ConvertedCurrencyDTO convertCurrency(String baseCurrency, String toCurrency, Double baseCurrencyAmount);
}
