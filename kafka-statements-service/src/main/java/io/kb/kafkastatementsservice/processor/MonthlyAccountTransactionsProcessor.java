package io.kb.kafkastatementsservice.processor;

import io.kb.kafkastatementsservice.common.JSONSerde;
import io.kb.kafkastatementsservice.model.TransactionInfo;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static io.kb.kafkastatementsservice.utils.Constants.*;

@Component
public class MonthlyAccountTransactionsProcessor {
  private static final Logger LOG =
      LoggerFactory.getLogger(MonthlyAccountTransactionsProcessor.class);

  @Autowired
  public void topology(StreamsBuilder streamsBuilder) {
    KStream<String, TransactionInfo> accountTransactionsStream =
        streamsBuilder.stream(
            ACCOUNT_TRANSACTIONS, Consumed.with(Serdes.String(), new JSONSerde<>()));
    accountTransactionsStream.print(Printed.toSysOut());
    LOG.info(ACCOUNT_TRANSACTIONS + " Source");
    KTable accountTransactionsMv =
        accountTransactionsStream
            .groupBy(
                (key, value) ->
                    value.getIbanAccount()
                        + GROUP_BY_DELIMITER
                        + getMonthAndYear(value.getValueDate()))
            .aggregate(
                ArrayList::new,
                (key, value, aggregate) -> {
                  aggregate.add(value);
                  return aggregate;
                },
                Materialized.as(STORE_NAME_MONTHLY_ACCOUNT_TRANSACTIONS_MV)
                    .withValueSerde(
                        new Serdes.ListSerde(ArrayList.class, new JSONSerde<TransactionInfo>())));
    LOG.info(STORE_NAME_MONTHLY_ACCOUNT_TRANSACTIONS_MV + " Materialized View (Store) Created");

    accountTransactionsMv.toStream().to(MONTHLY_ACCOUNT_TRANSACTIONS_TABLE);
    LOG.info(MONTHLY_ACCOUNT_TRANSACTIONS_TABLE + " Sink");
  }

  private static String getMonthAndYear(final String valueDate) {
    return DateTimeFormatter.ofPattern("MMyyyy")
        .format(LocalDate.parse(valueDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
  }
}
