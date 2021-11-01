package com.test.tdd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class BadBalanceException extends RuntimeException{

    public BadBalanceException(String message) {
        super(message);
    }
}
