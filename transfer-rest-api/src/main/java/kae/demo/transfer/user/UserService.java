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

  public List<User> getUsers() {
    return userRepository.getUsers().stream().map((this::toUserDTO)).collect(Collectors.toList());
  }

  private User toUserDTO(UserEntity userEntity) {
    return new User(userEntity.getId(), userEntity.getName());
  }
}
