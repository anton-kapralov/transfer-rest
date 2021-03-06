package kae.demo.transfer.api.it;

import static kae.demo.transfer.api.it.Endpoints.ACCOUNTS_PATH;
import static kae.demo.transfer.api.it.Endpoints.USERS_ENDPOINT;

import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import org.springframework.http.HttpStatus;

/** */
class AccountSeeder {

  private AccountSeeder() {}

  static void createAccount(JUnit4CitrusTestDesigner testDesigner) {
    testDesigner.http().client(USERS_ENDPOINT).send().post(ACCOUNTS_PATH);

    testDesigner
        .http()
        .client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.CREATED)
        .extractFromHeader("Location", "${location}");

    testDesigner.action(new SaveIdFromLocationTo("accountId"));
  }
}
