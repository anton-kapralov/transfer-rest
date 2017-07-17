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
public class AccountEntity {

  public static final long BANK_ACCOUNT_ID = 1;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private UserEntity user;

  public BigDecimal getBalance(EntityManager em) {
    em.createNativeQuery("LOCK TABLE TransactionEntity WRITE ").executeUpdate();

    final BigDecimal sumToAccount = TransactionEntity.getSumToAccount(em, id);
    final BigDecimal sumFromAccount = TransactionEntity.getSumFromAccount(em, id);
    return id != BANK_ACCOUNT_ID ? sumToAccount.subtract(sumFromAccount) :
        sumFromAccount.subtract(sumToAccount);
  }

}
