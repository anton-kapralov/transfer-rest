package kae.demo.transferrest.api.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@NamedQueries({
    @NamedQuery(
        name = TransactionEntity.QUERY_SUM_FROM_ACCOUNT,
        query = "SELECT SUM(t.amount) FROM TransactionEntity t " +
            "WHERE t.fromAccount.id=:accountId"),
    @NamedQuery(
        name = TransactionEntity.QUERY_SUM_TO_ACCOUNT,
        query = "SELECT SUM(t.amount) FROM TransactionEntity t " +
            "WHERE t.toAccount.id=:accountId")
})
public class TransactionEntity {

  public static final String QUERY_SUM_FROM_ACCOUNT = "TransactionEntity.querySumFromAccount";
  public static final String QUERY_SUM_TO_ACCOUNT = "TransactionEntity.querySumToAccount";

  private static final String PARAMETER_ACCOUNT_ID = "accountId";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private ZonedDateTime dateTime;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private AccountEntity fromAccount;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private AccountEntity toAccount;

  private BigDecimal amount;

  private String comment;

  public static BigDecimal getSumFromAccount(EntityManager em, long accountId) {
    return getSumByAccountId(em, QUERY_SUM_FROM_ACCOUNT, accountId);
  }

  public static BigDecimal getSumToAccount(EntityManager em, long accountId) {
    return getSumByAccountId(em, QUERY_SUM_TO_ACCOUNT, accountId);
  }

  private static BigDecimal getSumByAccountId(EntityManager em, String queryName, long accountId) {
    final Query querySumFromAccount = em.createNamedQuery(queryName);
    querySumFromAccount.setParameter(PARAMETER_ACCOUNT_ID, accountId);
    final BigDecimal result = (BigDecimal) querySumFromAccount.getSingleResult();
    return result != null ? result : BigDecimal.ZERO;
  }

}
