package com.mg.microservices.currency_exchange_service.controller;

import com.mg.microservices.currency_exchange_service.bean.CurrencyExchange;
import com.mg.microservices.currency_exchange_service.service.CurrencyExchangeService;
import com.mg.microservices.currency_exchange_service.service.LiveCurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final CurrencyExchangeService currencyExchangeService;

    public CurrencyExchangeController(LiveCurrencyService liveCurrencyService, CurrencyExchangeService currencyExchangeService) {
        this.liveCurrencyService = liveCurrencyService;
        this.currencyExchangeService = currencyExchangeService;
    }

    @GetMapping("from/{from}/to/{to}")
    public CurrencyExchange getCurrencyExchangePair(@PathVariable("from") String from,
                                                    @PathVariable("to") String to) {

        logger.info("Fetching exchange rate from {} to {}", from, to);

        try {
            return liveCurrencyService.getLiveCurrencyExchange(from, to);
        } catch (Exception e) {
            logger.warn("Live API failed, falling back to DB", e);
        }

        // fallback to DB
        CurrencyExchange exchange = currencyExchangeService.findByFromAndTo(from, to);
        if (exchange != null) {
            exchange.setServerEnvironment(exchange.getServerEnvironment() + " DB");
        }
        return exchange;
    }
}
