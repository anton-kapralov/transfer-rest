package kae.demo.transferrest.api.it;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.message.MessageType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import javax.json.Json;

import static org.hamcrest.Matchers.notNullValue;

/**
 *
 */
public class TransactionResourceIT extends JUnit4CitrusTestDesigner {

  private static final String USERS_ENDPOINT = "users";

  private static final String USER_ID = "1";
  private static final String ACCOUNT_ID = "1";
  private static final String TRANSACTIONS_PATH = "/" + USER_ID + "/accounts/" + ACCOUNT_ID + "/transactions";

  @Test
  @CitrusTest
  public void testCreateTransaction() throws Exception {
    http().client(USERS_ENDPOINT)
        .send()
        .post(TRANSACTIONS_PATH)
        .payload(Json.createObjectBuilder()
            .add("toAccountId", "3")
            .build()
            .toString());

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NO_CONTENT);
  }

  @Test
  @CitrusTest
  public void testGetTransactions() throws Exception {
    http().client(USERS_ENDPOINT)
        .send()
        .get(TRANSACTIONS_PATH);

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.[0].id", 1)
        .jsonPath("$.[0].fromAccountId", ACCOUNT_ID)
        .jsonPath("$.[0].toAccountId", notNullValue());
  }

  @Test
  @CitrusTest
  public void testGetTransaction() throws Exception {
    http().client(USERS_ENDPOINT)
        .send()
        .get(TRANSACTIONS_PATH + "/2");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.id", 2)
        .jsonPath("$.fromAccountId", ACCOUNT_ID)
        .jsonPath("$.toAccountId", notNullValue());
  }

  @Test
  @CitrusTest
  public void testDeleteTransaction() throws Exception {
    http().client(USERS_ENDPOINT)
        .send()
        .delete(TRANSACTIONS_PATH + "/3");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NO_CONTENT);
  }

}
