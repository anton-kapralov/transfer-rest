package kae.demo.transfer.persistence;

import java.util.function.Consumer;
import java.util.function.Function;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/** */
public class LocalEntityManagerFactory {

  private static volatile EntityManagerFactory emf;

  public static void initialize() {
    emf = Persistence.createEntityManagerFactory("TransferPU");
  }

  public static void close() {
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
      closeWithTransaction(em);
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

  public static <T> T executeAndReturnWithTransaction(Function<EntityManager, T> function) {
    EntityManager em = null;
    final T result;
    try {
      em = createEntityManager();
      final EntityTransaction transaction = em.getTransaction();
      transaction.begin();
      result = function.apply(em);
      transaction.commit();
    } finally {
      closeWithTransaction(em);
    }

    return result;
  }

  private static void closeWithTransaction(EntityManager em) {
    if (em != null) {
      final EntityTransaction transaction = em.getTransaction();
      if (transaction.isActive()) {
        transaction.rollback();
      }
      em.close();
    }
  }
}
