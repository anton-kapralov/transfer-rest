package kae.demo.transfer.api.it;

import static kae.demo.transfer.api.it.AccountSeeder.createAccount;
import static kae.demo.transfer.api.it.Endpoints.ACCOUNTS_PATH;
import static kae.demo.transfer.api.it.Endpoints.ACCOUNT_PATH;
import static kae.demo.transfer.api.it.Endpoints.USERS_ENDPOINT;
import static kae.demo.transfer.api.it.UserSeeder.createUser;
import static org.hamcrest.Matchers.greaterThan;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.message.MessageType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

/** */
public class AccountResourceIT extends JUnit4CitrusTestDesigner {

  @Test
  @CitrusTest
  public void testCreateAccount() {
    createUser(this, "Philip Kotler");
    createAccount(this);
  }

  @Test
  @CitrusTest
  public void testGetAccounts() {
    createUser(this, "Adam Smith");
    createAccount(this);

    http().client(USERS_ENDPOINT).send().get(ACCOUNTS_PATH);

    http()
        .client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.size()", greaterThan(0));
  }

  @Test
  @CitrusTest
  public void testGetAccount() {
    createUser(this, "John Maynard Keynes");
    createAccount(this);

    http().client(USERS_ENDPOINT).send().get(ACCOUNT_PATH);

    http()
        .client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.id", "${accountId}")
        .jsonPath("$.userId", "${userId}")
        .jsonPath("$.balance", "0");
  }

  @Test
  @CitrusTest
  public void testDeleteAccount() {
    createUser(this, "Karl Marx");
    createAccount(this);

    http().client(USERS_ENDPOINT).send().delete(ACCOUNT_PATH);

    http().client(USERS_ENDPOINT).receive().response(HttpStatus.NO_CONTENT);
  }
}
