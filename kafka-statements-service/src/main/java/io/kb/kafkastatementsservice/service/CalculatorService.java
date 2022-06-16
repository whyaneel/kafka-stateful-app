package io.kb.kafkastatementsservice.service;

import io.kb.kafkastatementsservice.model.ExchangeRate;
import io.kb.kafkastatementsservice.model.TransactionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CalculatorService {
  private static final String CHF = "CHF";
  private static final Logger LOG = LoggerFactory.getLogger(CalculatorService.class);

  public BigDecimal computeTotalCredit(
      List<TransactionInfo> accountTransactions, Map<String, ExchangeRate> exchangeRateMap) {
    List<TransactionInfo> creditedTransactions =
        accountTransactions.stream()
            .filter(
                tr ->
                    BigDecimal.valueOf(Double.parseDouble(tr.getAmount()))
                            .compareTo(BigDecimal.ZERO)
                        >= 0)
            .collect(Collectors.toList());

    BigDecimal totalCreditCHF =
        creditedTransactions.stream()
            .filter(tr -> "CHF".equalsIgnoreCase(tr.getCurrency()))
            .map(
                transactionInfo ->
                    BigDecimal.valueOf(Double.parseDouble(transactionInfo.getAmount())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalCreditOthers =
        creditedTransactions.stream()
            .filter(tr -> !CHF.equalsIgnoreCase(tr.getCurrency()))
            .map(
                tr ->
                    exchangeRateMap
                        .get(tr.getCurrency())
                        .getRate()
                        .multiply(BigDecimal.valueOf(Double.parseDouble(tr.getAmount()))))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    LOG.info("totalCredit {}", totalCreditCHF.add(totalCreditOthers));
    return totalCreditCHF.add(totalCreditOthers);
  }

  public BigDecimal computeTotalDebit(
      List<TransactionInfo> accountTransactions, Map<String, ExchangeRate> exchangeRateMap) {
    List<TransactionInfo> debitedTransactions =
        accountTransactions.stream()
            .filter(
                tr ->
                    BigDecimal.valueOf(Double.parseDouble(tr.getAmount()))
                            .compareTo(BigDecimal.ZERO)
                        < 0)
            .collect(Collectors.toList());

    BigDecimal totalCreditCHF =
        debitedTransactions.stream()
            .filter(tr -> "CHF".equalsIgnoreCase(tr.getCurrency()))
            .map(
                transactionInfo ->
                    BigDecimal.valueOf(Double.parseDouble(transactionInfo.getAmount())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalCreditOthers =
        debitedTransactions.stream()
            .filter(tr -> !CHF.equalsIgnoreCase(tr.getCurrency()))
            .map(
                tr ->
                    exchangeRateMap
                        .get(tr.getCurrency())
                        .getRate()
                        .multiply(BigDecimal.valueOf(Double.parseDouble(tr.getAmount()))))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    LOG.info("totalDebit {}", totalCreditCHF.add(totalCreditOthers));
    return totalCreditCHF.add(totalCreditOthers);
  }
}
