package io.kb.kafkastatementsservice.service;

import io.kb.kafkastatementsservice.client.XYZExchangeRateClient;
import io.kb.kafkastatementsservice.model.ExchangeRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRateService {
  private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateService.class);
  private final XYZExchangeRateClient xyzExchangeRateClient;

  @Autowired
  public ExchangeRateService(XYZExchangeRateClient xyzExchangeRateClient) {
    this.xyzExchangeRateClient = xyzExchangeRateClient;
  }

  public Map<String, ExchangeRate> exchangeRates() {
    List<ExchangeRate> exchangeRateList = xyzExchangeRateClient.fetchExchangeRates();
    Map<String, ExchangeRate> exchangeRates = new HashMap<>();
    if (!CollectionUtils.isEmpty(exchangeRateList)) {
      for (ExchangeRate exchangeRate : exchangeRateList) {
        exchangeRates.put(exchangeRate.getCurrency(), exchangeRate);
      }
    }
    LOG.info("exchangeRates {}", exchangeRates);
    return exchangeRates;
  }
}
