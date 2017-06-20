package kae.demo.transferrest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Transaction {

  private long id;

  private long fromAccountId;

  private long toAccountId;

  private BigDecimal amount;

  private String comment;

}
