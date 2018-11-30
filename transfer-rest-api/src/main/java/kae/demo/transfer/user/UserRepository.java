package kae.demo.transfer.user;

import java.util.List;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;

/** */
public class UserRepository {

  public UserEntity create(EntityManager em, String userName) {
    UserEntity userEntity = new UserEntity(0, userName);
    em.persist(userEntity);
    return userEntity;
  }

  public List<UserEntity> list(EntityManager em) {
    return em.createQuery("SELECT u FROM UserEntity u ORDER BY u.name", UserEntity.class)
        .getResultList();
  }

  public UserEntity get(EntityManager em, long id) {
    final UserEntity userEntity = em.find(UserEntity.class, id);
    if (userEntity == null) {
      // TODO: replace with domain exception.
      throw new NotFoundException("User has not been found by id " + id);
    }
    return userEntity;
  }

  public void update(EntityManager em, UserEntity userEntity) {
    em.merge(userEntity);
  }

  public void delete(EntityManager em, long id) {
    em.remove(get(em, id));
  }
}
