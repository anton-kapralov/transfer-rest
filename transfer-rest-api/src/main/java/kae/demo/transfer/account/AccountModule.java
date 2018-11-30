package kae.demo.transfer.account;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/** */
@Slf4j
public class AccountModule extends AbstractBinder {

  @Override
  protected void configure() {
    log.info("Configuring account module bindings.");
    bindAsContract(AccountService.class);
    bindAsContract(AccountRepository.class);
    bindAsContract(AccountBalanceRepository.class);
  }
}
