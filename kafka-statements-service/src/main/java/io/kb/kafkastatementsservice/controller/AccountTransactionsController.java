package io.kb.kafkastatementsservice.controller;

import io.kb.kafkastatementsservice.model.TransactionInfo;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static io.kb.kafkastatementsservice.utils.Constants.STORE_NAME_ACCOUNT_TRANSACTIONS_MV;
import static io.kb.kafkastatementsservice.utils.KafkaStreamUtils.getReadOnlyKeyValueStore;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/statements/v1/", produces = APPLICATION_JSON_VALUE)
public class AccountTransactionsController {
  private static final Logger LOG = LoggerFactory.getLogger(AccountTransactionsController.class);

  private final StreamsBuilderFactoryBean factoryBean;

  @Autowired
  public AccountTransactionsController(StreamsBuilderFactoryBean factoryBean) {
    this.factoryBean = factoryBean;
  }

  @PostMapping(path = "/transactions/{ibanAccount}", consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<TransactionInfo> getAccountTransactions(@PathVariable final String ibanAccount) {
    final ReadOnlyKeyValueStore<String, TransactionInfo> store =
        getReadOnlyKeyValueStore(factoryBean, STORE_NAME_ACCOUNT_TRANSACTIONS_MV);

    final List<TransactionInfo> accountTransactions =
        (List<TransactionInfo>) store.get(ibanAccount);
    if (CollectionUtils.isEmpty(accountTransactions)) {
      LOG.info("zero transactions for ibanAccount {}", ibanAccount);
      return new ArrayList<>();
    }
    return accountTransactions;
  }
}
