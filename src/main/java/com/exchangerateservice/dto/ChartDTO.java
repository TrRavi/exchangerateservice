package com.exchangerateservice.dto;

import java.util.List;

public class ChartDTO {

    private String baseCurrency;
    private String currency;
    private List<String> chartURL;

    public ChartDTO(String baseCurrency, String currency, List<String> chartURL) {
        this.baseCurrency = baseCurrency;
        this.currency = currency;
        this.chartURL = chartURL;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<String> getChartURL() {
        return chartURL;
    }

    public void setChartURL(List<String> chartURL) {
        this.chartURL = chartURL;
    }
}
