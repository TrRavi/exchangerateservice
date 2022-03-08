package com.exchangerateservice.exception.handler;

import com.exchangerateservice.exception.CurrencyNotSupported;
import com.exchangerateservice.exception.ThirdPartyServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler(value = ThirdPartyServerException.class)
    public ResponseEntity<ExceptionFormat> handleThirdPartyServerException(ThirdPartyServerException thirdPartyServerException){
        log.error("ThirdPartyServerException Exception occurred");
        return new ResponseEntity<>(new ExceptionFormat(thirdPartyServerException.getHttpStatus(),thirdPartyServerException.getMessage()),
                thirdPartyServerException.getHttpStatus());
    }

    @ExceptionHandler(value = CurrencyNotSupported.class)
    public ResponseEntity<ExceptionFormat> handleCurrencyNotSupported(CurrencyNotSupported currencyNotSupported){
        log.error("CurrencyNotSupported Exception occurred");
        return new ResponseEntity<>(new ExceptionFormat(HttpStatus.BAD_REQUEST,currencyNotSupported.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

}
