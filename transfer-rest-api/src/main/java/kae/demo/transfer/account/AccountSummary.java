package kae.demo.transfer.account;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class AccountSummary {

  private final BigDecimal sumToAccount;
  private final BigDecimal sumFromAccount;
}
