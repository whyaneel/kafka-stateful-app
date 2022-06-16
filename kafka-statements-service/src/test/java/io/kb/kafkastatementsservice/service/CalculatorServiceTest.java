package io.kb.kafkastatementsservice.service;

import io.kb.kafkastatementsservice.model.ExchangeRate;
import io.kb.kafkastatementsservice.model.TransactionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {

  @Test
  void computeTotalCredit() {
    CalculatorService calculatorService = new CalculatorService();
    Map<String, ExchangeRate> exchangeRates = new HashMap<>();
    exchangeRates.put(
        "GBP",
        ExchangeRate.ExchangeRateBuilder.anExchangeRate()
            .withCurrency("GBP")
            .withRate(BigDecimal.valueOf(1.190101))
            .build());
    exchangeRates.put(
        "EUR",
        ExchangeRate.ExchangeRateBuilder.anExchangeRate()
            .withCurrency("EUR")
            .withRate(BigDecimal.valueOf(1.020101))
            .build());

    List<TransactionInfo> transactionInfoList = new ArrayList<>();
    transactionInfoList.add(
        TransactionInfo.TransactionInfoBuilder.aTransactionInfo()
            .withUniqueIdentifier(UUID.randomUUID().toString())
            .withIbanAccount("CH99-0")
            .withAmount("85.1")
            .withValueDate("08-06-2022")
            .withCurrency("EUR")
            .withDescription("Cash Payment")
            .build());

    transactionInfoList.add(
        TransactionInfo.TransactionInfoBuilder.aTransactionInfo()
            .withUniqueIdentifier(UUID.randomUUID().toString())
            .withIbanAccount("CH99-0")
            .withAmount("105.2")
            .withValueDate("08-06-2022")
            .withCurrency("GBP")
            .withDescription("Cash Payment")
            .build());

    assertEquals(
        BigDecimal.valueOf(212.0092203),
        calculatorService.computeTotalCredit(transactionInfoList, exchangeRates));
  }

  @Test
  void computeTotalDebit() {
    CalculatorService calculatorService = new CalculatorService();
    Map<String, ExchangeRate> exchangeRates = new HashMap<>();
    exchangeRates.put(
        "GBP",
        ExchangeRate.ExchangeRateBuilder.anExchangeRate()
            .withCurrency("GBP")
            .withRate(BigDecimal.valueOf(1.190101))
            .build());
    exchangeRates.put(
        "EUR",
        ExchangeRate.ExchangeRateBuilder.anExchangeRate()
            .withCurrency("EUR")
            .withRate(BigDecimal.valueOf(1.020101))
            .build());

    List<TransactionInfo> transactionInfoList = new ArrayList<>();
    transactionInfoList.add(
        TransactionInfo.TransactionInfoBuilder.aTransactionInfo()
            .withUniqueIdentifier(UUID.randomUUID().toString())
            .withIbanAccount("CH99-0")
            .withAmount("-85.1")
            .withValueDate("08-06-2022")
            .withCurrency("EUR")
            .withDescription("Cash Payment")
            .build());

    transactionInfoList.add(
        TransactionInfo.TransactionInfoBuilder.aTransactionInfo()
            .withUniqueIdentifier(UUID.randomUUID().toString())
            .withIbanAccount("CH99-0")
            .withAmount("-105.2")
            .withValueDate("08-06-2022")
            .withCurrency("GBP")
            .withDescription("Cash Payment")
            .build());

    assertEquals(
        BigDecimal.valueOf(-212.0092203),
        calculatorService.computeTotalDebit(transactionInfoList, exchangeRates));
  }
}
