package com.exchangerateservice.exception;

import com.exchangerateservice.entity.CurrencyReferenceRate;

public class CurrencyNotSupported extends RuntimeException{

    public CurrencyNotSupported(String currency){
        super("currency Code '"+currency+"' Not Supported");
    }

    public CurrencyNotSupported(CurrencyReferenceRate currencyReferenceRate) {
    }
}
