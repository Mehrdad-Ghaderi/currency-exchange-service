package com.mg.microservices.currency_exchange_service.bean;

import java.math.BigDecimal;

/**
 * Created by Mehrdad Ghaderi, S&M
 * Date: 8/7/2025
 * Time: 12:43 AM
 */
public class CurrencyExchange {
    private Long id;
    private String from;
    private String to;
    private BigDecimal rate;

    public CurrencyExchange() {
    }

    public CurrencyExchange(Long id, String from, String to, BigDecimal rate) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
