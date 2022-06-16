package io.kb.kafkastatementsservice.utils;

import io.kb.kafkastatementsservice.model.TransactionInfo;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

public class KafkaStreamUtils {
  private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamUtils.class);

  // TODO return type to be generic
  public static ReadOnlyKeyValueStore<String, TransactionInfo> getReadOnlyKeyValueStore(
      StreamsBuilderFactoryBean factoryBean, final String storeName) {
    KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
    if (kafkaStreams == null) {
      throw new RuntimeException("kafkaStreams instance not found");
    }

    while (true) {
      try {
        return kafkaStreams.store(
            StoreQueryParameters.fromNameAndType(storeName, QueryableStoreTypes.keyValueStore()));
      } catch (InvalidStateStoreException ignored) {
        LOG.error("Store is not yet ready for querying");
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          LOG.error("InterruptedException while waiting the Store to be computed");
        }
      }
    }
  }
}
