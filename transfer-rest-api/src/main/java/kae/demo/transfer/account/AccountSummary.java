package kae.demo.transfer.account;

import java.math.BigDecimal;
import lombok.Value;

@Value
public class AccountSummary {

  private final BigDecimal sumToAccount;
  private final BigDecimal sumFromAccount;
}
