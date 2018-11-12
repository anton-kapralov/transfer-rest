package kae.demo.transfer.user;

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
    final UserEntity userEntity = userRepository.create(user.getName());
    return userEntity.getId();
  }

  public List<User> list() {
    return userRepository.list().stream().map((this::toUserDTO)).collect(Collectors.toList());
  }

  public User get(long id) {
    return toUserDTO(userRepository.get(id));
  }

  public void update(long id, User userUpdate) {
    final UserEntity userEntity = userRepository.get(id);

    final String name = userUpdate.getName();
    if (name != null && !name.isEmpty()) {
      userEntity.setName(name);
    }

    userRepository.update(userEntity);
  }

  public void delete(long id) {
    userRepository.delete(id);
  }

  private User toUserDTO(UserEntity userEntity) {
    return new User(userEntity.getId(), userEntity.getName());
  }
}
