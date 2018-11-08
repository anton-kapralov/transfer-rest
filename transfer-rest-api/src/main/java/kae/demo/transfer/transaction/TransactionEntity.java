package kae.demo.transfer.transaction;

import kae.demo.transfer.account.AccountEntity;
import kae.demo.transfer.account.AccountSummary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

/** */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@NamedNativeQueries({
  @NamedNativeQuery(
      name = TransactionEntity.QUERY_ACCOUNT_BALANCE,
      query =
          "SELECT amountTo, amountFrom FROM "
              + "(SELECT SUM(t.amount) as amountTo FROM TransactionEntity t WHERE t.toAccount_id=?), "
              + "(SELECT SUM(t.amount) as amountFrom FROM TransactionEntity t WHERE t.fromAccount_id=?)")
})
public class TransactionEntity {

  public static final String QUERY_ACCOUNT_BALANCE = "TransactionEntity.queryAccountBalance";

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

  public static Optional<AccountSummary> getSummaryByAccountId(EntityManager em, long accountId) {
    final Query queryForSum = em.createNamedQuery(QUERY_ACCOUNT_BALANCE);
    queryForSum.setParameter(1, accountId);
    queryForSum.setParameter(2, accountId);
    final Object[] result = (Object[]) queryForSum.getSingleResult();
    if (result != null) {
      final BigDecimal sumToAccount = ofNullable((BigDecimal) result[0]);
      final BigDecimal sumFromAccount = ofNullable((BigDecimal) result[1]);
      return Optional.of(new AccountSummary(sumToAccount, sumFromAccount));
    } else {
      return Optional.empty();
    }
  }

  private static BigDecimal ofNullable(BigDecimal result) {
    return result != null ? result : BigDecimal.ZERO;
  }
}
