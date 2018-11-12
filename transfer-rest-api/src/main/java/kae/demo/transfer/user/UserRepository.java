package kae.demo.transfer.user;

import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeAndReturn;
import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeWithTransaction;

import java.util.List;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;

/** */
public class UserRepository {

  public UserEntity create(String userName) {
    UserEntity userEntity = new UserEntity(0, userName);
    executeWithTransaction((em) -> em.persist(userEntity));
    return userEntity;
  }

  public List<UserEntity> list() {
    return executeAndReturn(
        (em) ->
            em.createQuery("SELECT u FROM UserEntity u ORDER BY u.name", UserEntity.class)
                .getResultList());
  }

  public UserEntity get(long id) {
    return executeAndReturn((em) -> get(em, id));
  }

  private UserEntity get(EntityManager em, long id) {
    final UserEntity userEntity = em.find(UserEntity.class, id);
    if (userEntity == null) {
      // TODO: replace with domain exception.
      throw new NotFoundException("User has not been found by id " + id);
    }
    return userEntity;
  }

  public void update(UserEntity userEntity) {
    executeWithTransaction((em) -> em.merge(userEntity));
  }

  public void delete(long id) {
    executeWithTransaction((em) -> em.remove(get(em, id)));
  }
}
