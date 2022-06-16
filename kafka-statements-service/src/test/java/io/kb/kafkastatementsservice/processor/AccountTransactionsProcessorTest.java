package io.kb.kafkastatementsservice.processor;

import io.kb.kafkastatementsservice.common.JSONSerde;
import io.kb.kafkastatementsservice.model.TransactionInfo;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static io.kb.kafkastatementsservice.utils.Constants.ACCOUNT_TRANSACTIONS;
import static io.kb.kafkastatementsservice.utils.Constants.ACCOUNT_TRANSACTIONS_TABLE;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;
import static org.assertj.core.api.Assertions.assertThat;

class AccountTransactionsProcessorTest {

  private AccountTransactionsProcessor accountTransactionsProcessor;

  @BeforeEach
  void setUp() {
    accountTransactionsProcessor = new AccountTransactionsProcessor();
  }

  @Test
  void givenInputMessages_whenProcessedWithTopology_thenOutputShouldMatchWithExpected() {
    StreamsBuilder streamsBuilder = new StreamsBuilder();
    accountTransactionsProcessor.topology(streamsBuilder);
    Topology topology = streamsBuilder.build();

    Properties props = new Properties();
    props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, JSONSerde.class);
    try (TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, props)) {
      TestInputTopic<String, TransactionInfo> inputTopic =
          topologyTestDriver.createInputTopic(
              ACCOUNT_TRANSACTIONS, new StringSerializer(), new JSONSerde<>());
      TestOutputTopic<String, List<TransactionInfo>> outputTopic =
          topologyTestDriver.createOutputTopic(
              ACCOUNT_TRANSACTIONS_TABLE,
              new StringDeserializer(),
              new Serdes.ListSerde(ArrayList.class, new JSONSerde<TransactionInfo>())
                  .deserializer());

      List<TransactionInfo> transactions = new ArrayList<>();
      transactions.add(
          new TransactionInfo(
              "3c04fcce-65fa-4115-9441-2781f6706ca7",
              "85",
              "CHF",
              "CH93-0000-0000-0000-0000-0",
              "08-07-2022",
              "Online payment CHF"));
      transactions.add(
          new TransactionInfo(
              "3c04fcce-65fa-4115-9441-2781f6706cb7",
              "75",
              "CHF",
              "CH93-0000-0000-0000-0000-0",
              "08-07-2022",
              "Online payment CHF"));

      transactions.forEach(tr -> inputTopic.pipeInput(tr.getUniqueIdentifier(), tr));

      List<TransactionInfo> actualOutput = outputTopic.readValuesToList().get(1);

      assertThat(transactions).contains(actualOutput.get(0), actualOutput.get(1));
    }
  }
}
