package io.kb.kafkastatementsservice.model;

import java.math.BigDecimal;

public class ExchangeRate {
  private String currency;
  private String quoteCurrency = "CHF";
  private BigDecimal rate;

  public ExchangeRate(String currency, String quoteCurrency, BigDecimal rate) {
    this.currency = currency;
    this.quoteCurrency = quoteCurrency;
    this.rate = rate;
  }

  public String getCurrency() {
    return currency;
  }

  public String getQuoteCurrency() {
    return quoteCurrency;
  }

  public BigDecimal getRate() {
    return rate;
  }

  @Override
  public String toString() {
    return "ExchangeRate{"
        + "currency='"
        + currency
        + '\''
        + ", quoteCurrency='"
        + quoteCurrency
        + '\''
        + ", rate="
        + rate
        + '}';
  }

  public static final class ExchangeRateBuilder {
    private String currency;
    private String quoteCurrency = "CHF";
    private BigDecimal rate;

    private ExchangeRateBuilder() {}

    public static ExchangeRateBuilder anExchangeRate() {
      return new ExchangeRateBuilder();
    }

    public ExchangeRateBuilder withCurrency(String currency) {
      this.currency = currency;
      return this;
    }

    public ExchangeRateBuilder withQuoteCurrency(String quoteCurrency) {
      this.quoteCurrency = quoteCurrency;
      return this;
    }

    public ExchangeRateBuilder withRate(BigDecimal rate) {
      this.rate = rate;
      return this;
    }

    public ExchangeRate build() {
      return new ExchangeRate(currency, quoteCurrency, rate);
    }
  }
}
