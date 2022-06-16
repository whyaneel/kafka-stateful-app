package io.kb.kafkastatementsservice.model;

import io.kb.kafkastatementsservice.common.JSONSerdeCompatible;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TransactionInfo that = (TransactionInfo) o;
    return Objects.equals(uniqueIdentifier, that.uniqueIdentifier)
        && Objects.equals(amount, that.amount)
        && Objects.equals(currency, that.currency)
        && Objects.equals(ibanAccount, that.ibanAccount)
        && Objects.equals(valueDate, that.valueDate)
        && Objects.equals(description, that.description);
  }

  public static final class TransactionInfoBuilder {
    private String uniqueIdentifier;
    private String amount;
    private String currency;
    private String ibanAccount;
    private String valueDate;
    private String description;

    private TransactionInfoBuilder() {}

    public static TransactionInfoBuilder aTransactionInfo() {
      return new TransactionInfoBuilder();
    }

    public TransactionInfoBuilder withUniqueIdentifier(String uniqueIdentifier) {
      this.uniqueIdentifier = uniqueIdentifier;
      return this;
    }

    public TransactionInfoBuilder withAmount(String amount) {
      this.amount = amount;
      return this;
    }

    public TransactionInfoBuilder withCurrency(String currency) {
      this.currency = currency;
      return this;
    }

    public TransactionInfoBuilder withIbanAccount(String ibanAccount) {
      this.ibanAccount = ibanAccount;
      return this;
    }

    public TransactionInfoBuilder withValueDate(String valueDate) {
      this.valueDate = valueDate;
      return this;
    }

    public TransactionInfoBuilder withDescription(String description) {
      this.description = description;
      return this;
    }

    public TransactionInfo build() {
      return new TransactionInfo(
          uniqueIdentifier, amount, currency, ibanAccount, valueDate, description);
    }
  }
}
