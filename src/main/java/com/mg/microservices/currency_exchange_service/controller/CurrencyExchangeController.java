package com.mg.microservices.currency_exchange_service.controller;

import com.mg.microservices.currency_exchange_service.bean.CurrencyExchange;
import com.mg.microservices.currency_exchange_service.repository.CurrencyExchangeRepository;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created by Mehrdad Ghaderi, S&M
 * Date: 8/7/2025
 * Time: 12:40 AM
 */
@RestController
public class CurrencyExchangeController {

    private final Environment environment;
    private final CurrencyExchangeRepository repository;

    public CurrencyExchangeController(Environment environment, CurrencyExchangeRepository repository) {
        this.environment = environment;
        this.repository = repository;
    }

    @GetMapping("currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange getExchangeValue(@PathVariable("from") String from,
                                             @PathVariable("to") String to) {

        Optional<CurrencyExchange> foundPair = repository.findByFromAndTo(from, to);
        if (foundPair.isEmpty()) {
            throw new RuntimeException("Pair " + from + " to " + to + " was not found");
        }
        String port = environment.getProperty("local.server.port");
        CurrencyExchange currencyExchange = foundPair.get();
        currencyExchange.setEnvironment(port);
        return currencyExchange;
    }

}
