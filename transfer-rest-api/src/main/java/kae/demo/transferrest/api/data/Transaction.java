package kae.demo.transferrest.api.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

}
