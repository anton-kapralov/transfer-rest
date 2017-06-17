package kae.demo.transferrest.api.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 */
@WebListener
public class LocalEntityManagerFactory implements ServletContextListener {

  private static EntityManagerFactory emf;

  @Override
  public void contextInitialized(ServletContextEvent event) {
    emf = Persistence.createEntityManagerFactory("TransferPU");
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    if (emf != null) {
      emf.close();
    }
  }

  private static EntityManager createEntityManager() {
    if (emf == null) {
      throw new IllegalStateException("Context is not initialized yet.");
    }
    return emf.createEntityManager();
  }

  public static void executeWithTransaction(Consumer<EntityManager> consumer) {
    EntityManager em = null;
    try {
      em = createEntityManager();
      final EntityTransaction transaction = em.getTransaction();
      transaction.begin();
      consumer.accept(em);
      transaction.commit();
    } finally {
      if (em != null) {
        final EntityTransaction transaction = em.getTransaction();
        if (transaction.isActive()) {
          transaction.rollback();
        }
        em.close();
      }
    }
  }

  public static <T> T executeAndReturn(Function<EntityManager, T> function) {
    EntityManager em = null;
    try {
      em = createEntityManager();
      return function.apply(em);
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }
}
