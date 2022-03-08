package com.exchangerateservice.exception;

import org.springframework.http.HttpStatus;

public class ThirdPartyServerException extends RuntimeException{

    private HttpStatus httpStatus;
    public ThirdPartyServerException(String message){
        super(message);
    }
    public ThirdPartyServerException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
