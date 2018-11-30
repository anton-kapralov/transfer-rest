package kae.demo.transfer.account;

import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeAndReturn;
import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeAndReturnWithTransaction;
import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeWithTransaction;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import kae.demo.transfer.user.UserEntity;
import kae.demo.transfer.user.UserRepository;

/** */
public class AccountService {

  private final UserRepository userRepository;
  private final AccountRepository accountRepository;

  @Inject
  public AccountService(UserRepository userRepository, AccountRepository accountRepository) {
    this.userRepository = userRepository;
    this.accountRepository = accountRepository;
  }

  public long create(long userId) {
    AccountEntity accountEntity =
        executeAndReturnWithTransaction(
            (em) -> {
              UserEntity userEntity = userRepository.get(em, userId);
              return accountRepository.create(em, userEntity);
            });
    return accountEntity.getId();
  }

  public List<Account> list(long userId) {
    return executeAndReturn(em -> accountRepository.list(em, userId))
        .stream()
        .map(this::toAccountDTO)
        .collect(Collectors.toList());
  }

  public Account get(long userId, long id) {
    final AccountEntity accountEntity =
        executeAndReturn((em) -> accountRepository.get(em, userId, id));
    return toAccountDTO(accountEntity);
  }

  public void delete(long userId, long id) {
    executeWithTransaction((em) -> accountRepository.delete(em, userId, id));
  }

  private Account toAccountDTO(AccountEntity accountEntity) {
    return new Account(
        accountEntity.getId(), accountEntity.getUser().getId(), accountEntity.getBalance());
  }
}
