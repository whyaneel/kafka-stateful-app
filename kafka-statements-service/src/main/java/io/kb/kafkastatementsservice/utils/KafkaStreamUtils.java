package io.kb.kafkastatementsservice.utils;

import io.kb.kafkastatementsservice.exception.AccountNotFoundException;
import io.kb.kafkastatementsservice.exception.KafkaStreamsException;
import io.kb.kafkastatementsservice.model.TransactionInfo;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.List;

import static io.kb.kafkastatementsservice.utils.Constants.*;

public class KafkaStreamUtils {
  private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamUtils.class);

  KafkaStreamUtils() {}

  private static <T> ReadOnlyKeyValueStore<String, T> getReadOnlyKeyValueStore(
      StreamsBuilderFactoryBean factoryBean, final String storeName) {
    KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
    if (kafkaStreams == null) {
      throw new KafkaStreamsException("kafkaStreams instance not found");
    }

    while (true) {
      try {
        return kafkaStreams.store(
            StoreQueryParameters.fromNameAndType(storeName, QueryableStoreTypes.keyValueStore()));
      } catch (InvalidStateStoreException ignored) {
        LOG.error("Store is not yet ready for querying");
        try {
          Thread.sleep(200);
        } catch (InterruptedException ignored2) {
          LOG.error("InterruptedException while waiting the Store to be computed");
        }
      }
    }
  }

  public static List<TransactionInfo> getAllTransactionInfo(
      StreamsBuilderFactoryBean factoryBean, final String ibanAccount) {
    final ReadOnlyKeyValueStore<String, List<TransactionInfo>> store =
        getReadOnlyKeyValueStore(factoryBean, STORE_NAME_ACCOUNT_TRANSACTIONS_MV);
    return store.get(ibanAccount);
  }

  public static List<TransactionInfo> getMonthlyTransactionInfo(
      StreamsBuilderFactoryBean factoryBean, final String monthAndYear, final String ibanAccount) {
    final ReadOnlyKeyValueStore<String, List<TransactionInfo>> store =
        getReadOnlyKeyValueStore(factoryBean, STORE_NAME_MONTHLY_ACCOUNT_TRANSACTIONS_MV);
    return store.get(ibanAccount + GROUP_BY_DELIMITER + monthAndYear);
  }

  // For simplicity considering Single Account per UniqueId
  public static String getAccount(final String uniqueId) {
    LOG.info("getAccount UniqueId {}", uniqueId);
    String account = null;
    if (uniqueId.trim().equalsIgnoreCase("P-0123456789")) {
      account = "CH93-0000-0000-0000-0000-1";
    } else if (uniqueId.trim().equalsIgnoreCase("P-9876543210")) {
      account = "CH93-0000-0000-0000-0000-0";
    }
    if (null == account) {
      LOG.info("No Account found for user's uniqueId {}", uniqueId);
      throw new AccountNotFoundException("No Account found");
    }
    return account;
  }
}
