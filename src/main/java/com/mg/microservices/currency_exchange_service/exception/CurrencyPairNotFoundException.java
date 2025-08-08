package com.mg.microservices.currency_exchange_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Mehrdad Ghaderi, S&M
 * Date: 8/7/2025
 * Time: 9:07 PM
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CurrencyPairNotFoundException extends RuntimeException {

    public CurrencyPairNotFoundException(String from, String to) {
        super("Currency pair not found: " + from + " to " + to);
    }
}
