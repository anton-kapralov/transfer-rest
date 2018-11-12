package kae.demo.transfer.user;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/** */
@Slf4j
public class UserModule extends AbstractBinder {

  @Override
  protected void configure() {
    log.info("Configuring user module bindings.");
    bindAsContract(UserService.class);
    bindAsContract(UserRepository.class);
  }
}
