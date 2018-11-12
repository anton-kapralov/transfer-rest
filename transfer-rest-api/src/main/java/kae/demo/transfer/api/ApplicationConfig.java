package kae.demo.transfer.api;

import javax.ws.rs.ApplicationPath;
import kae.demo.transfer.user.UserModule;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.ResourceConfig;

/** */
@ApplicationPath("rest")
@Slf4j
public class ApplicationConfig extends ResourceConfig {

  public ApplicationConfig() {
    log.info("Initializing application config.");

    packages("kae.demo.transfer", "com.fasterxml.jackson.jaxrs.json");

    register(new UserModule());
  }
}
