package kae.demo.transferrest.api.it;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.message.MessageType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import javax.json.Json;

/**
 *
 */
public class AccountResourceIT extends JUnit4CitrusTestDesigner {

  private static final String USERS_ENDPOINT = "users";
  private static final String USER_ID = "1";
  private static final String ACCOUNTS_PATH = "/" + USER_ID + "/accounts";

  @Test
  @CitrusTest
  public void testCreateAccount() throws Exception {
    http().client(USERS_ENDPOINT)
        .send()
        .post(ACCOUNTS_PATH);

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NO_CONTENT);
  }

  @Test
  @CitrusTest
  public void testGetAccounts() throws Exception {
    http().client(USERS_ENDPOINT)
        .send()
        .get(ACCOUNTS_PATH);

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.[0].id", 1)
        .jsonPath("$.[0].userId", USER_ID);
  }

  @Test
  @CitrusTest
  public void testGetAccount() throws Exception {
    http().client(USERS_ENDPOINT)
        .send()
        .get(ACCOUNTS_PATH + "/2");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.id", 2)
        .jsonPath("$.userId", USER_ID);
  }

  @Test
  @CitrusTest
  public void testDeleteAccount() throws Exception {
    http().client(USERS_ENDPOINT)
        .send()
        .delete(ACCOUNTS_PATH + "/3");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NO_CONTENT);
  }

}
