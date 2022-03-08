package com.exchangerateservice.dto;

public class ConvertedCurrencyDTO {

    private String baseCurrency;
    private String toCurrency;
    private Double baseCurrencyAmount;
    private Double toCurrencyAmount;


    public ConvertedCurrencyDTO(String baseCurrency, String toCurrency, Double baseCurrencyAmount, Double toCurrencyAmount) {
        this.baseCurrency = baseCurrency;
        this.toCurrency = toCurrency;
        this.baseCurrencyAmount = baseCurrencyAmount;
        this.toCurrencyAmount = toCurrencyAmount;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Double getBaseCurrencyAmount() {
        return baseCurrencyAmount;
    }

    public void setBaseCurrencyAmount(Double baseCurrencyAmount) {
        this.baseCurrencyAmount = baseCurrencyAmount;
    }

    public Double getToCurrencyAmount() {
        return toCurrencyAmount;
    }

    public void setToCurrencyAmount(Double toCurrencyAmount) {
        this.toCurrencyAmount = toCurrencyAmount;
    }
}
