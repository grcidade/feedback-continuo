package br.com.dbc.vimserdev.feedbackcontinuo.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleException extends Exception {
    public HttpStatus statusCode;
    public BusinessRuleException(String message, HttpStatus status) {
        super(message);
        this.statusCode = status;
    }
}
