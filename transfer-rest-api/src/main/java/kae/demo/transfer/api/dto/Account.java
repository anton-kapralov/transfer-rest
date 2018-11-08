package kae.demo.transfer.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
