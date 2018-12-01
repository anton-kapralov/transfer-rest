package kae.demo.transfer.account;

import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.ws.rs.BadRequestException;
import kae.demo.transfer.user.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class AccountEntity {

  public static final long BANK_ACCOUNT_ID = 1;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private UserEntity user;

  @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, optional = false)
  private AccountBalanceEntity balance;

  public AccountEntity(long id, UserEntity user) {
    this.id = id;
    this.user = user;
    this.balance = new AccountBalanceEntity(this);
  }

  public boolean isBankAccount() {
    return id == BANK_ACCOUNT_ID;
  }

  public BigDecimal getBalance() {
    return isBankAccount() ? balance.getValue().negate() : balance.getValue();
  }

  public void updateBalance(BigDecimal delta) {
    if (delta.compareTo(BigDecimal.ZERO) < 0
        && !isBankAccount()
        && !hasEnoughFunds(delta.negate())) {
      throw new BadRequestException("Not enough funds");
    }

    balance.setValue(balance.getValue().add(delta));
  }

  private boolean hasEnoughFunds(BigDecimal withdrawalAmount) {
    return getBalance().compareTo(withdrawalAmount) >= 0;
  }

  public boolean idAndUserIdEqualTo(long id, long userId) {
    return this.id == id && user.getId() == userId;
  }
}
