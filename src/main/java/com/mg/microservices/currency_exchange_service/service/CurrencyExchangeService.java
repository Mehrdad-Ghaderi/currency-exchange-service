package com.mg.microservices.currency_exchange_service.service;

import com.mg.microservices.currency_exchange_service.bean.CurrencyExchange;
import com.mg.microservices.currency_exchange_service.exception.CurrencyPairNotFoundException;
import com.mg.microservices.currency_exchange_service.repository.CurrencyExchangeRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Created by Mehrdad Ghaderi, S&M
 * Date: 8/15/2025
 * Time: 12:30 PM
 */
@Service
public class CurrencyExchangeService {

    private final Environment environment;
    private final CurrencyExchangeRepository repository;

    public CurrencyExchangeService(Environment environment, CurrencyExchangeRepository repository) {
        this.environment = environment;
        this.repository = repository;
    }

    public CurrencyExchange findByFromAndTo(String from, String to) {
        return repository.findByFromAndToIgnoreCase(from, to)
                .map(currencyExchange -> {
                    currencyExchange.setServerEnvironment(
                            environment.getProperty("local.server.port"));
                    return currencyExchange;
                })
                .orElseThrow(() -> new CurrencyPairNotFoundException(from, to));

    }
}
