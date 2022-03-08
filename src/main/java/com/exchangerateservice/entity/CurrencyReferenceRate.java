package com.exchangerateservice.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "CURRENCY_REFERENCE_RATE", uniqueConstraints = @UniqueConstraint(columnNames = {"CURRENCY_CODE","DATE"}))
@Entity
public class CurrencyReferenceRate {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "CURRENCY_CODE", nullable = false)
    private String currencyCode;

    @Column(name = "DATE", nullable = false)
    private Date date;

    @Column(name = "REFERENCE_RATE", nullable = false)
    private Double referenceRate;

    public CurrencyReferenceRate() {

    }

    public CurrencyReferenceRate(Long id, String currencyCode, Date date, Double referenceRate) {
        this.id = id;
        this.currencyCode = currencyCode;
        this.date = date;
        this.referenceRate = referenceRate;
    }

    public CurrencyReferenceRate(String currencyCode, Date date, Double referenceRate) {
        this.currencyCode = currencyCode;
        this.date = date;
        this.referenceRate = referenceRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getExchangeRate() {
        return referenceRate;
    }

    public void setExchangeRate(Double referenceRate) {
        this.referenceRate = referenceRate;
    }
}
