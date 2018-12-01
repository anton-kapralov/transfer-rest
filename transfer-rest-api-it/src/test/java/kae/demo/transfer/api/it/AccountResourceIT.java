package kae.demo.transfer.api.it;

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
  public void testCreateAccount() throws Exception {
    ITHelper.createUser(this, "Philip Kotler");
    ITHelper.createAccount(this);
  }

  @Test
  @CitrusTest
  public void testGetAccounts() throws Exception {
    ITHelper.createUser(this, "Adam Smith");
    ITHelper.createAccount(this);

    http().client(ITHelper.USERS_ENDPOINT).send().get(ITHelper.ACCOUNTS_PATH);

    http()
        .client(ITHelper.USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.size()", greaterThan(0));
  }

  @Test
  @CitrusTest
  public void testGetAccount() throws Exception {
    ITHelper.createUser(this, "John Maynard Keynes");
    ITHelper.createAccount(this);

    http().client(ITHelper.USERS_ENDPOINT).send().get(ITHelper.ACCOUNTS_PATH + "/${accountId}");

    http()
        .client(ITHelper.USERS_ENDPOINT)
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
    ITHelper.createUser(this, "Karl Marx");
    ITHelper.createAccount(this);

    http().client(ITHelper.USERS_ENDPOINT).send().delete(ITHelper.ACCOUNTS_PATH + "/${accountId}");

    http().client(ITHelper.USERS_ENDPOINT).receive().response(HttpStatus.NO_CONTENT);
  }
}
