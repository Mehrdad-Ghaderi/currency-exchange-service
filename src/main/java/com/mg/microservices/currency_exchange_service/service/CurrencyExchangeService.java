package com.mg.microservices.currency_exchange_service.service;

import com.mg.microservices.currency_exchange_service.repository.CurrencyExchangeRepository;

/**
 * Created by Mehrdad Ghaderi, S&M
 * Date: 8/15/2025
 * Time: 12:30 PM
 */
public class CurrencyExchangeService {

    public CurrencyExchangeService(CurrencyExchangeRepository repository) {
        this.repository = repository;
    }

    private final CurrencyExchangeRepository repository;

}
