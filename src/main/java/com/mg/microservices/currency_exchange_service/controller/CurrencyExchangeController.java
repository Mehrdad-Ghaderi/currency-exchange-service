package com.mg.microservices.currency_exchange_service.controller;

import com.mg.microservices.currency_exchange_service.bean.CurrencyExchange;
import com.mg.microservices.currency_exchange_service.exception.CurrencyPairNotFoundException;
import com.mg.microservices.currency_exchange_service.repository.CurrencyExchangeRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by Mehrdad Ghaderi, S&M
 * Date: 8/7/2025
 * Time: 12:40 AM
 */
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class CurrencyExchangeController {

    private final Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    private final Environment environment;
    private final CurrencyExchangeRepository repository;

    public CurrencyExchangeController(Environment environment, CurrencyExchangeRepository repository) {
        this.environment = environment;
        this.repository = repository;
    }

    @GetMapping("currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange getCurrencyExchangePair(@PathVariable("from") String from,
                                             @PathVariable("to") String to) {

        logger.info("Fetching exchange rate from {} to {}", from, to);

        return repository.findByFromAndToIgnoreCase(from, to)
                .map(exchange -> {
                    exchange.setServerEnvironment(environment.getProperty("local.server.port"));
                    return exchange;
                })
                .orElseThrow(() -> new CurrencyPairNotFoundException(from, to));
    }

}
