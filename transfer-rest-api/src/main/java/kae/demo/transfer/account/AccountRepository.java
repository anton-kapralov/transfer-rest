package kae.demo.transfer.account;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.ws.rs.NotFoundException;
import kae.demo.transfer.user.UserEntity;

/** */
public class AccountRepository {

  public AccountEntity create(EntityManager em, UserEntity userEntity) {
    AccountEntity accountEntity = new AccountEntity(0, userEntity);
    em.persist(accountEntity);
    return accountEntity;
  }

  public List<AccountEntity> list(EntityManager em, long userId) {
    final CriteriaBuilder cb = em.getCriteriaBuilder();
    final CriteriaQuery<AccountEntity> cq = cb.createQuery(AccountEntity.class);
    final Root<AccountEntity> root = cq.from(AccountEntity.class);
    final ParameterExpression<Long> userIdParameter = cb.parameter(Long.class);

    cq.select(root)
        .where(cb.equal(root.get("user").get("id"), userIdParameter))
        .orderBy(cb.asc(root.get("id")));

    final TypedQuery<AccountEntity> query = em.createQuery(cq);
    query.setParameter(userIdParameter, userId);

    return query.getResultList();
  }

  public AccountEntity get(EntityManager em, long id) {
    return em.find(AccountEntity.class, id);
  }

  public AccountEntity get(EntityManager em, long userId, long id) {
    final AccountEntity accountEntity = em.find(AccountEntity.class, id);
    if (accountEntity == null || accountEntity.getUser().getId() != userId) {
      throw new NotFoundException(
          "User account has not been found by id " + id + " and userId " + userId);
    }
    return accountEntity;
  }

  public void delete(EntityManager em, long userId, long id) {
    em.remove(get(em, userId, id));
  }
}
