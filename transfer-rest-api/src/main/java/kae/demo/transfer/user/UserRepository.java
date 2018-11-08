package kae.demo.transfer.user;

import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeAndReturn;

import java.util.List;

/** */
public class UserRepository {
  public List<UserEntity> getUsers() {
    return executeAndReturn(
        (em) ->
            em.createQuery("SELECT u FROM UserEntity u ORDER BY u.name", UserEntity.class)
                .getResultList());
  }
}
