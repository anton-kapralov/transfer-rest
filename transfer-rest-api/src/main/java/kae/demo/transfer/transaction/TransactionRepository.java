package kae.demo.transfer.transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.ws.rs.NotFoundException;
import kae.demo.transfer.account.AccountEntity;

/** */
public class TransactionRepository {

  public TransactionEntity create(
      EntityManager em,
      ZonedDateTime dateTime,
      AccountEntity fromAccount,
      AccountEntity toAccount,
      BigDecimal amount,
      String comment) {
    TransactionEntity transactionEntity =
        new TransactionEntity(0, dateTime, fromAccount, toAccount, amount, comment);
    em.persist(transactionEntity);
    return transactionEntity;
  }

  public List<TransactionEntity> list(EntityManager em, long userId, long accountId) {
    final CriteriaBuilder cb = em.getCriteriaBuilder();
    final CriteriaQuery<TransactionEntity> cq = cb.createQuery(TransactionEntity.class);
    final Root<TransactionEntity> root = cq.from(TransactionEntity.class);
    final ParameterExpression<Long> userIdParameter = cb.parameter(Long.class);
    final ParameterExpression<Long> accountIdParameter = cb.parameter(Long.class);

    cq.select(root)
        .where(
            cb.or(
                cb.and(
                    cb.equal(root.get("fromAccount").get("id"), accountIdParameter),
                    cb.equal(root.get("fromAccount").get("user").get("id"), userIdParameter)),
                cb.and(
                    cb.equal(root.get("toAccount").get("id"), accountIdParameter),
                    cb.equal(root.get("toAccount").get("user").get("id"), userIdParameter))))
        .orderBy(cb.asc(root.get("id")));

    final TypedQuery<TransactionEntity> query = em.createQuery(cq);
    query.setParameter(userIdParameter, userId);
    query.setParameter(accountIdParameter, accountId);

    return query.getResultList();
  }

  public TransactionEntity get(EntityManager em, long userId, long accountId, long id) {
    final TransactionEntity transactionEntity = em.find(TransactionEntity.class, id);
    if (transactionEntity != null && transactionEntity.belongsTo(userId, accountId)) {
      return transactionEntity;
    }
    throw new NotFoundException(
        "Transaction has not been found by id "
            + id
            + " and accountId "
            + accountId
            + " and userId "
            + userId);
  }

  public void delete(EntityManager em, long userId, long accountId, long id) {
    em.remove(get(em, userId, accountId, id));
  }
}
