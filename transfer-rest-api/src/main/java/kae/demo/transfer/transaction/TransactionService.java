package kae.demo.transfer.transaction;

import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeAndReturn;
import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeAndReturnWithTransaction;
import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeWithTransaction;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import kae.demo.transfer.account.AccountEntity;
import kae.demo.transfer.account.AccountRepository;

/** */
public class TransactionService {

  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  @Inject
  public TransactionService(
      AccountRepository accountRepository, TransactionRepository transactionRepository) {
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository;
  }

  public long create(long userId, long accountId, Transaction transaction) {
    // TODO: Add retry in case of OptimisticLockException.

    TransactionEntity transactionEntity =
        executeAndReturnWithTransaction(
            em -> {
              AccountEntity fromAccount = accountRepository.get(em, userId, accountId);
              fromAccount.updateBalance(transaction.getAmount().negate());
              em.merge(fromAccount);

              AccountEntity toAccount = accountRepository.get(em, transaction.getToAccountId());
              toAccount.updateBalance(transaction.getAmount());
              em.merge(toAccount);

              return transactionRepository.create(
                  em,
                  ZonedDateTime.now(),
                  fromAccount,
                  toAccount,
                  transaction.getAmount(),
                  transaction.getComment());
            });
    return transactionEntity.getId();
  }

  public List<Transaction> list(long userId, long accountId) {
    return executeAndReturn(em -> transactionRepository.list(em, userId, accountId))
        .stream()
        .map(this::toTransactionDTO)
        .collect(Collectors.toList());
  }

  public Transaction get(long userId, long accountId, long id) {
    return toTransactionDTO(
        executeAndReturn(em -> transactionRepository.get(em, userId, accountId, id)));
  }

  public void delete(long userId, long accountId, long id) {
    executeWithTransaction(em -> transactionRepository.delete(em, userId, accountId, id));
  }

  private Transaction toTransactionDTO(TransactionEntity transactionEntity) {
    return new Transaction(
        transactionEntity.getId(),
        transactionEntity.getDateTime(),
        transactionEntity.getFromAccount().getId(),
        transactionEntity.getToAccount().getId(),
        transactionEntity.getAmount(),
        transactionEntity.getComment());
  }
}
