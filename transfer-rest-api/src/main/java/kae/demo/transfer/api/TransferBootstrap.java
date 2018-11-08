package kae.demo.transfer.api;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import kae.demo.transfer.persistence.BasicDataLoader;
import kae.demo.transfer.persistence.LocalEntityManagerFactory;

/** */
@WebListener
public class TransferBootstrap implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent event) {
    LocalEntityManagerFactory.initialize();
    BasicDataLoader.load();
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    LocalEntityManagerFactory.close();
  }
}
