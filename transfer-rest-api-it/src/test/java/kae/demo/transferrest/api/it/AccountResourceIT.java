package kae.demo.transferrest.api.it;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.message.MessageType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static kae.demo.transferrest.api.it.ITHelper.*;
import static org.hamcrest.Matchers.greaterThan;

/**
 *
 */
public class AccountResourceIT extends JUnit4CitrusTestDesigner {

  @Test
  @CitrusTest
  public void testCreateAccount() throws Exception {
    createUser(this, "Philip Kotler");
    createAccount(this);
  }

  @Test
  @CitrusTest
  public void testGetAccounts() throws Exception {
    createUser(this, "Adam Smith");
    createAccount(this);

    http().client(USERS_ENDPOINT)
        .send()
        .get(ACCOUNTS_PATH);

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.size()", greaterThan(0));
  }

  @Test
  @CitrusTest
  public void testGetAccount() throws Exception {
    createUser(this, "John Maynard Keynes");
    createAccount(this);

    http().client(USERS_ENDPOINT)
        .send()
        .get(ACCOUNTS_PATH + "/${accountId}");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.id", "${accountId}")
        .jsonPath("$.userId", "${userId}")
        .jsonPath("$.balance", "0");
  }

  @Test
  @CitrusTest
  public void testDeleteAccount() throws Exception {
    createUser(this, "Karl Marx");
    createAccount(this);

    http().client(USERS_ENDPOINT)
        .send()
        .delete(ACCOUNTS_PATH + "/${accountId}");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NO_CONTENT);
  }

}
