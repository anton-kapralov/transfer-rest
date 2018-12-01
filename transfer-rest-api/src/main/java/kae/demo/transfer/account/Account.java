package kae.demo.transfer.account;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Account {

  private long id;

  private long userId;

  private BigDecimal balance;
}
