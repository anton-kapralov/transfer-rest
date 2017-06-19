package kae.demo.transferrest.api.it;

import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import org.springframework.http.HttpStatus;

import javax.json.Json;

import static org.junit.Assert.assertNotNull;

/**
 *
 */
class ITHelper {
  static final String USERS_ENDPOINT = "users";
  static final String ACCOUNTS_PATH = "/${userId}/accounts";

  static void createUser(JUnit4CitrusTestDesigner testDesigner, String name) {
    testDesigner.http().client(USERS_ENDPOINT)
        .send()
        .post()
        .payload(Json.createObjectBuilder()
            .add("name", name)
            .build()
            .toString());

    testDesigner.http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.CREATED)
        .extractFromHeader("Location", "${location}");

    testDesigner.action(new AbstractTestAction() {
      @Override
      public void doExecute(TestContext context) {
        context.setVariable("userId", extractIdFromLocation(context));
      }
    });
  }

  static void createAccount(JUnit4CitrusTestDesigner testDesigner) {
    testDesigner.http().client(USERS_ENDPOINT)
        .send()
        .post(ACCOUNTS_PATH);

    testDesigner.http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.CREATED)
        .extractFromHeader("Location", "${location}");

    testDesigner.action(new AbstractTestAction() {
      @Override
      public void doExecute(TestContext context) {
        context.setVariable("accountId", extractIdFromLocation(context));
      }
    });
  }

  private static long extractIdFromLocation(TestContext context) {
    final String location = context.getVariable("location");
    assertNotNull(location);
    return Long.valueOf(location.substring(location.lastIndexOf("/") + 1));
  }
}
