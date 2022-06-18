package io.kb.kafkastatementsservice.controller;

import io.kb.kafkastatementsservice.model.ExchangeRate;
import io.kb.kafkastatementsservice.model.PageableTransactionResponse;
import io.kb.kafkastatementsservice.model.TransactionInfo;
import io.kb.kafkastatementsservice.service.CalculatorService;
import io.kb.kafkastatementsservice.service.ExchangeRateService;
import io.kb.kafkastatementsservice.service.JwtService;
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
import static io.kb.kafkastatementsservice.utils.KafkaStreamUtils.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = API_CONTEXT + VERSION, produces = APPLICATION_JSON_VALUE)
public class AccountTransactionsController {
  private static final Logger LOG = LoggerFactory.getLogger(AccountTransactionsController.class);

  private final StreamsBuilderFactoryBean factoryBean;
  private final CalculatorService calculatorService;
  private final ExchangeRateService exchangeRateService;
  private final JwtService jwtService;

  @Autowired
  public AccountTransactionsController(
      StreamsBuilderFactoryBean factoryBean,
      CalculatorService calculatorService,
      ExchangeRateService exchangeRateService,
      JwtService jwtService) {
    this.factoryBean = factoryBean;
    this.calculatorService = calculatorService;
    this.exchangeRateService = exchangeRateService;
    this.jwtService = jwtService;
  }

  @GetMapping(path = "/transactions", produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public PageableTransactionResponse getAccountTransactions(
      @RequestHeader(value = "Authorization") final String bearerToken) {
    final String ibanAccount =
        getAccount(jwtService.getId(bearerToken.substring(BEARER_TOKEN_PREFIX.length())));
    final List<TransactionInfo> accountTransactions =
        getAllTransactionInfo(factoryBean, ibanAccount);
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

  @GetMapping(path = "/transactions/month/{monthAndYear}", produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public PageableTransactionResponse getAccountTransactions(
      @PathVariable final String monthAndYear,
      @RequestHeader(value = "Authorization") final String bearerToken) {
    final String ibanAccount =
        getAccount(jwtService.getId(bearerToken.substring(BEARER_TOKEN_PREFIX.length())));
    final List<TransactionInfo> accountTransactions =
        getMonthlyTransactionInfo(factoryBean, monthAndYear, ibanAccount);
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
