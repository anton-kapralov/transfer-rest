package kae.demo.transfer.persistence;

import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeWithTransaction;

import kae.demo.transfer.account.AccountEntity;
import kae.demo.transfer.user.UserEntity;

/** */
public class BasicDataLoader {

  public static void load() {
    final UserEntity bank = new UserEntity(UserEntity.BANK_USER_ID, "Bank");
    final AccountEntity bankAccount = new AccountEntity(AccountEntity.BANK_ACCOUNT_ID, bank);

    executeWithTransaction(
        em -> {
          em.persist(bank);
          em.persist(bankAccount);
        });
  }
}
