package com.mg.microservices.currency_exchange_service.service;

import com.mg.microservices.currency_exchange_service.bean.CurrencyExchange;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

/**
 * Created by Mehrdad Ghaderi, S&M
 * Date: 8/15/2025
 * Time: 3:09 AM
 */
@Service
public class LiveCurrencyService {

    private final WebClient webClient;

    public LiveCurrencyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.frankfurter.app").build();
    }

    @Cacheable(value = "exchangeRates", key = "#from + '_' + #to")
    public CurrencyExchange getLiveCurrencyExchange(String from, String to) {
        BigDecimal rate = fetchRate(from, to);

        CurrencyExchange exchange = new CurrencyExchange();
        exchange.setFrom(from.toUpperCase());
        exchange.setTo(to.toUpperCase());
        exchange.setRate(rate);
        exchange.setDate(LocalDate.now()); // or use API date if available
        exchange.setServerEnvironment("LIVE_API");

        return exchange;
    }

    public BigDecimal fetchRate(String from, String to) {
        Map<String, Object> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block(Duration.ofSeconds(5));

        Map<String, Object> rates = (Map<String, Object>) response.get("rates");
        return new BigDecimal(rates.get(to).toString());
    }
}

