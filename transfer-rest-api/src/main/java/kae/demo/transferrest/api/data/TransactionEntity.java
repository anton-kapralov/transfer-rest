package kae.demo.transferrest.api.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private AccountEntity fromAccount;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private AccountEntity toAccount;

  private BigDecimal amount;

  private String comment;

}
