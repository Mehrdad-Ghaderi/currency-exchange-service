package com.mg.microservices.currency_exchange_service.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
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
    public BigDecimal getRate(String from, String to) {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/latest")
                            .queryParam("from", from)
                            .queryParam("to", to)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block(Duration.ofSeconds(5));

            if (response != null && response.containsKey("rates")) {
                Map<String, Object> rates = (Map<String, Object>) response.get("rates");
                return new BigDecimal(rates.get(to).toString());
            }
        } catch (Exception e) {
            // log.warn("Failed to fetch live rate, falling back to H2 database", e);
        }

        // Fallback: hard-coded sample rates in H2 DB
        BigDecimal fallbackRate = getFallbackRate(from, to);
        if (fallbackRate != null) return fallbackRate;

        throw new RuntimeException("Failed to fetch exchange rate for " + from + " -> " + to);
    }

    private BigDecimal getFallbackRate(String from, String to) {
        // Implement H2 lookup logic here
        // Example:
        // return repository.findByFromAndTo(from, to).map(ExchangeRateEntity::getRate).orElse(null);
        return null;
    }
    // Map JSON response
    static class ExchangeResponse {
        private BigDecimal result;
        public BigDecimal getResult() { return result; }
        public void setResult(BigDecimal result) { this.result = result; }
    }
}

