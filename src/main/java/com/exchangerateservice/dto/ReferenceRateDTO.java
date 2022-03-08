package com.exchangerateservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ReferenceRateDTO {

    private String baseCurrency;
    private String currency;
    private Double exchangeRate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern= "yyyy-MM-dd")
    private Date date;

    public ReferenceRateDTO(String baseCurrency, String currency, Double exchangeRate, Date date) {
        this.baseCurrency = baseCurrency;
        this.currency = currency;
        this.exchangeRate = exchangeRate;
        this.date = date;
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

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
