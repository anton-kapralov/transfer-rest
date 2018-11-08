package kae.demo.transfer.account;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

/** */
@Entity
@Data
@NoArgsConstructor
public class AccountBalanceEntity {

  @Id @OneToOne private AccountEntity account;

  private BigDecimal value;

  @Version private int version;

  public AccountBalanceEntity(AccountEntity account) {
    this.account = account;
    value = BigDecimal.ZERO;
  }
}
