package kae.demo.transfer.transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Transaction {

  private long id;

  private ZonedDateTime dateTime;

  private long fromAccountId;

  private long toAccountId;

  private BigDecimal amount;

  private String comment;
}
