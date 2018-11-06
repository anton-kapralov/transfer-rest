package kae.demo.transferrest.api.data;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class AccountSummary {

  private final BigDecimal sumToAccount;
  private final BigDecimal sumFromAccount;
}
