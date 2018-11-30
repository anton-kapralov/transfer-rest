package kae.demo.transfer.transaction;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/** */
@Slf4j
public class TransactionModule extends AbstractBinder {

  @Override
  protected void configure() {
    log.info("Configuring transaction module bindings.");
    bindAsContract(TransactionService.class);
    bindAsContract(TransactionRepository.class);
  }
}
