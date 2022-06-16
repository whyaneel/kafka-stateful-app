package io.kb.kafkastatementsservice.client;

import io.kb.kafkastatementsservice.model.ExchangeRate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "exchange-rate-service", url = "${exchange-rate-service.url}")
public interface XYZExchangeRateClient {
  
  @GetMapping(value = "/ok")
  String health();
  
  @GetMapping(value = "/rates")
  List<ExchangeRate> fetchExchangeRates();
}
