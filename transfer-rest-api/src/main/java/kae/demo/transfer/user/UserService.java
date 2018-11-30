package kae.demo.transfer.user;

import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeAndReturn;
import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeAndReturnWithTransaction;
import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeWithTransaction;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

/** */
public class UserService {

  private final UserRepository userRepository;

  @Inject
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public long create(User user) {
    final UserEntity userEntity =
        executeAndReturnWithTransaction((em) -> userRepository.create(em, user.getName()));
    return userEntity.getId();
  }

  public List<User> list() {
    return executeAndReturn(userRepository::list)
        .stream()
        .map((this::toUserDTO))
        .collect(Collectors.toList());
  }

  public User get(long id) {
    return toUserDTO(executeAndReturn((em) -> userRepository.get(em, id)));
  }

  public void update(long id, User userUpdate) {
    executeWithTransaction(
        (em) -> {
          final UserEntity userEntity = userRepository.get(em, id);

          final String name = userUpdate.getName();
          if (name != null && !name.isEmpty()) {
            userEntity.setName(name);
          }

          userRepository.update(em, userEntity);
        });
  }

  public void delete(long id) {
    executeWithTransaction((em) -> userRepository.delete(em, id));
  }

  private User toUserDTO(UserEntity userEntity) {
    return new User(userEntity.getId(), userEntity.getName());
  }
}
