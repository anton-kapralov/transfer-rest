package kae.demo.transferrest.api.it;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.message.MessageType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static kae.demo.transferrest.api.it.ITHelper.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

/**
 *
 */
public class TransactionResourceIT extends JUnit4CitrusTestDesigner {

  @Test
  @CitrusTest
  public void testCreateTransaction() throws Exception {
    final long amount = 5000000000L;
    createTransaction(this, "Alisher Usmanov", "Dmitry Medvedev", amount, "This is not a bribe");
    http().client(USERS_ENDPOINT)
        .send()
        .get(ACCOUNTS_PATH + "/${accountId}");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.id", "${accountId}")
        .jsonPath("$.userId", "${userId}")
        .jsonPath("$.balance", -amount);
  }

  @Test
  @CitrusTest
  public void testGetTransactions() throws Exception {
    createTransaction(this, "Mark Zuckerberg", "Pavel Durov", 100L, "Thanks for beer!");

    http().client(USERS_ENDPOINT)
        .send()
        .get(TRANSACTIONS_PATH);

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.size()", greaterThan(0));
  }

  @Test
  @CitrusTest
  public void testGetTransaction() throws Exception {
    final long amount = 300L;
    final String comment = "For Catch-22";
    createTransaction(this, "Kurt Vonnegut", "Joseph Heller", amount, comment);

    http().client(USERS_ENDPOINT)
        .send()
        .get(TRANSACTIONS_PATH + "/${transactionId}");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.id", "${transactionId}")
        .jsonPath("$.fromAccountId", notNullValue())
        .jsonPath("$.toAccountId", notNullValue())
        .jsonPath("$.amount", amount)
        .jsonPath("$.comment", comment);
  }

  @Test
  @CitrusTest
  public void testDeleteTransaction() throws Exception {
    createTransaction(this, "Anonymous", "Alexey Navalny", 1000L, "For the great justice!");

    http().client(USERS_ENDPOINT)
        .send()
        .delete(TRANSACTIONS_PATH + "/${transactionId}");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NO_CONTENT);

    http().client(USERS_ENDPOINT)
        .send()
        .get(TRANSACTIONS_PATH + "/${transactionId}");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NOT_FOUND)
        .messageType(MessageType.JSON);
  }

}
