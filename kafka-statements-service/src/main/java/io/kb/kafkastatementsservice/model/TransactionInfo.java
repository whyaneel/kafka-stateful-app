package io.kb.kafkastatementsservice.model;

import io.kb.kafkastatementsservice.common.JSONSerdeCompatible;

public class TransactionInfo implements JSONSerdeCompatible {
  private String uniqueIdentifier;
  private String amount;
  private String currency;
  private String ibanAccount;
  private String valueDate;
  private String description;

  public TransactionInfo() {}

  public TransactionInfo(
      String uniqueIdentifier,
      String amount,
      String currency,
      String ibanAccount,
      String valueDate,
      String description) {
    this.uniqueIdentifier = uniqueIdentifier;
    this.amount = amount;
    this.currency = currency;
    this.ibanAccount = ibanAccount;
    this.valueDate = valueDate;
    this.description = description;
  }

  public String getUniqueIdentifier() {
    return uniqueIdentifier;
  }

  public String getAmount() {
    return amount;
  }

  public String getCurrency() {
    return currency;
  }

  public String getIbanAccount() {
    return ibanAccount;
  }

  public String getValueDate() {
    return valueDate;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return "TransactionInfo{"
        + "uniqueIdentifier='"
        + uniqueIdentifier
        + '\''
        + ", amount='"
        + amount
        + '\''
        + ", currency='"
        + currency
        + '\''
        + ", ibanAccount='"
        + ibanAccount
        + '\''
        + ", valueDate='"
        + valueDate
        + '\''
        + ", description='"
        + description
        + '\''
        + '}';
  }
}
