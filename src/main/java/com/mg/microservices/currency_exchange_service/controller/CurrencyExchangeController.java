package com.mg.microservices.currency_exchange_service.controller;

import com.mg.microservices.currency_exchange_service.bean.CurrencyExchange;
import com.mg.microservices.currency_exchange_service.exception.CurrencyPairNotFoundException;
import com.mg.microservices.currency_exchange_service.repository.CurrencyExchangeRepository;
import com.mg.microservices.currency_exchange_service.service.LiveCurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Mehrdad Ghaderi, S&M
 * Date: 8/7/2025
 * Time: 12:40 AM
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/currency-exchange")
public class CurrencyExchangeController {

    private final Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    private final LiveCurrencyService liveCurrencyService;
    private final Environment environment;
    private final CurrencyExchangeRepository repository;

    public CurrencyExchangeController(LiveCurrencyService liveCurrencyService, Environment environment, CurrencyExchangeRepository repository) {
        this.liveCurrencyService = liveCurrencyService;
        this.environment = environment;
        this.repository = repository;
    }

    @GetMapping("db/from/{from}/to/{to}")
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


    @GetMapping("live/from/{from}/to/{to}")
    public ResponseEntity<Map<String, Object>> getExchangeValue(
            @PathVariable String from,
            @PathVariable String to) {

        BigDecimal rate = liveCurrencyService.getRate(from, to);

        Map<String, Object> response = new HashMap<>();
        response.put("from", from.toUpperCase());
        response.put("to", to.toUpperCase());
        response.put("exchangeRate", rate);

        return ResponseEntity.ok(response);
    }

}
