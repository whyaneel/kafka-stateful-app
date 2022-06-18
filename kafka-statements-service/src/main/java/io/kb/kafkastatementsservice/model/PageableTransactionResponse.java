package io.kb.kafkastatementsservice.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PageableTransactionResponse {

  private List<TransactionInfo> transactions = new ArrayList<>();
  private BigDecimal totalCredit = BigDecimal.ZERO;
  private BigDecimal totalDebit = BigDecimal.ZERO;
  
  //TODO Pageable info (pageNo, pageSize, total)

  public List<TransactionInfo> getTransactions() {
    return transactions;
  }

  public BigDecimal getTotalCredit() {
    return totalCredit;
  }

  public BigDecimal getTotalDebit() {
    return totalDebit;
  }

  @Override
  public String toString() {
    return "PageableTransactionResponse{"
        + "transactions="
        + transactions
        + ", totalCredit="
        + totalCredit
        + ", totalDebit="
        + totalDebit
        + '}';
  }
  
  public static final class PageableTransactionResponseBuilder {
    private List<TransactionInfo> transactions = new ArrayList<>();
    private BigDecimal totalCredit = BigDecimal.ZERO;
    private BigDecimal totalDebit = BigDecimal.ZERO;
    
    private PageableTransactionResponseBuilder() {
    }
    
    public static PageableTransactionResponseBuilder aPageableTransactionResponse() {
      return new PageableTransactionResponseBuilder();
    }
    
    public PageableTransactionResponseBuilder withTransactions(List<TransactionInfo> transactions) {
      this.transactions = transactions;
      return this;
    }
    
    public PageableTransactionResponseBuilder withTotalCredit(BigDecimal totalCredit) {
      this.totalCredit = totalCredit;
      return this;
    }
    
    public PageableTransactionResponseBuilder withTotalDebit(BigDecimal totalDebit) {
      this.totalDebit = totalDebit;
      return this;
    }
    
    public PageableTransactionResponse build() {
      PageableTransactionResponse pageableTransactionResponse = new PageableTransactionResponse();
      pageableTransactionResponse.totalCredit = this.totalCredit;
      pageableTransactionResponse.transactions = this.transactions;
      pageableTransactionResponse.totalDebit = this.totalDebit;
      return pageableTransactionResponse;
    }
  }
}
