package kae.demo.transferrest.api.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Optional;

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

  public boolean isBankAccount() {
    return id == BANK_ACCOUNT_ID;
  }

  public BigDecimal getBalance(EntityManager em) {
    final Optional<AccountSummary> optionalSummary =
        TransactionEntity.getSummaryByAccountId(em, id);
    return optionalSummary
        .map(summary -> {
          final BigDecimal balance = summary.getSumToAccount().subtract(summary.getSumFromAccount());
          return isBankAccount() ? balance.negate() : balance;
        })
        .orElse(BigDecimal.ZERO);
  }

  public boolean hasEnoughFunds(EntityManager em, BigDecimal withdrawalAmount) {
    return getBalance(em).compareTo(withdrawalAmount) >= 0;
  }

}
