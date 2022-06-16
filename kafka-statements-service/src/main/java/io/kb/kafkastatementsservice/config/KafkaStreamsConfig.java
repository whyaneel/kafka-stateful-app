package io.kb.kafkastatementsservice.config;

import io.kb.kafkastatementsservice.common.JSONSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.*;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsConfig {

  private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsConfig.class);

  @Value(value = "${spring.kafka.streams.application-id}")
  private String applicationId;

  @Value(value = "${spring.kafka.streams.bootstrap-servers}")
  private String bootstrapServers;

  @Value(value = "${spring.kafka.streams.replication-factor}")
  private String replicationFactor;

  @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
  KafkaStreamsConfiguration kStreamsConfig() {
    Map<String, Object> props = new HashMap<>();
    props.put(APPLICATION_ID_CONFIG, applicationId);
    props.put(CLIENT_ID_CONFIG, applicationId);
    props.put(GROUP_ID_CONFIG, applicationId);
    props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(REPLICATION_FACTOR_CONFIG, replicationFactor);
    props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, JSONSerde.class);
    LOG.info("Kafka Streams Config is Set");
    return new KafkaStreamsConfiguration(props);
  }
}
