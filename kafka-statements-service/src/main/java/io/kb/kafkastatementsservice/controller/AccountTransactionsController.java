package io.kb.kafkastatementsservice.controller;

import io.kb.kafkastatementsservice.model.ExchangeRate;
import io.kb.kafkastatementsservice.model.PageableTransactionResponse;
import io.kb.kafkastatementsservice.model.TransactionInfo;
import io.kb.kafkastatementsservice.service.CalculatorService;
import io.kb.kafkastatementsservice.service.ExchangeRateService;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static io.kb.kafkastatementsservice.utils.Constants.*;
import static io.kb.kafkastatementsservice.utils.KafkaStreamUtils.getReadOnlyKeyValueStore;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/statements/v1/", produces = APPLICATION_JSON_VALUE)
public class AccountTransactionsController {
  private static final Logger LOG = LoggerFactory.getLogger(AccountTransactionsController.class);

  private final StreamsBuilderFactoryBean factoryBean;
  private final CalculatorService calculatorService;
  private final ExchangeRateService exchangeRateService;

  @Autowired
  public AccountTransactionsController(
      StreamsBuilderFactoryBean factoryBean,
      CalculatorService calculatorService,
      ExchangeRateService exchangeRateService) {
    this.factoryBean = factoryBean;
    this.calculatorService = calculatorService;
    this.exchangeRateService = exchangeRateService;
  }

  @PostMapping(path = "/transactions/{ibanAccount}", consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public PageableTransactionResponse getAccountTransactions(
      @PathVariable final String ibanAccount) {
    final ReadOnlyKeyValueStore<String, TransactionInfo> store =
        getReadOnlyKeyValueStore(factoryBean, STORE_NAME_ACCOUNT_TRANSACTIONS_MV);

    final List<TransactionInfo> accountTransactions =
        (List<TransactionInfo>) store.get(ibanAccount);
    if (CollectionUtils.isEmpty(accountTransactions)) {
      LOG.info("zero transactions for ibanAccount {}", ibanAccount);
      return PageableTransactionResponse.PageableTransactionResponseBuilder
          .aPageableTransactionResponse()
          .build();
    }

    Map<String, ExchangeRate> exchangeRateMap = exchangeRateService.exchangeRates();
    return PageableTransactionResponse.PageableTransactionResponseBuilder
        .aPageableTransactionResponse()
        .withTransactions(accountTransactions)
        .withTotalCredit(calculatorService.computeTotalCredit(accountTransactions, exchangeRateMap))
        .withTotalDebit(calculatorService.computeTotalDebit(accountTransactions, exchangeRateMap))
        .build();
  }

  @PostMapping(
      path = "/transactions/{ibanAccount}/month/{monthAndYear}",
      consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public PageableTransactionResponse getAccountTransactions(
      @PathVariable final String ibanAccount, @PathVariable final String monthAndYear) {
    final ReadOnlyKeyValueStore<String, TransactionInfo> store =
        getReadOnlyKeyValueStore(factoryBean, STORE_NAME_MONTHLY_ACCOUNT_TRANSACTIONS_MV);

    final List<TransactionInfo> accountTransactions =
        (List<TransactionInfo>) store.get(ibanAccount + GROUP_BY_DELIMITER + monthAndYear);
    if (CollectionUtils.isEmpty(accountTransactions)) {
      LOG.info("zero transactions for ibanAccount {} for MMyyyy {}", ibanAccount, monthAndYear);
      return PageableTransactionResponse.PageableTransactionResponseBuilder
          .aPageableTransactionResponse()
          .build();
    }
    Map<String, ExchangeRate> exchangeRateMap = exchangeRateService.exchangeRates();
    return PageableTransactionResponse.PageableTransactionResponseBuilder
        .aPageableTransactionResponse()
        .withTransactions(accountTransactions)
        .withTotalCredit(calculatorService.computeTotalCredit(accountTransactions, exchangeRateMap))
        .withTotalDebit(calculatorService.computeTotalDebit(accountTransactions, exchangeRateMap))
        .build();
  }
}
