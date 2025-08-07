package com.mg.microservices.currency_exchange_service.controller;

import com.mg.microservices.currency_exchange_service.bean.CurrencyExchange;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Created by Mehrdad Ghaderi, S&M
 * Date: 8/7/2025
 * Time: 12:40 AM
 */
@RestController
public class CurrencyExchangeController {

    @GetMapping("currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange getExchangeValue(@PathVariable("from") String from,
                                             @PathVariable("to") String to) {
        return new CurrencyExchange(1000L, from, to, BigDecimal.valueOf(1.37));
    }

}
