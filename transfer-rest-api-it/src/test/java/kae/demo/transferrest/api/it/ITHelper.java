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
  static final String TRANSACTIONS_PATH = ACCOUNTS_PATH + "/${accountId}/transactions";
  static final String BANK_ACCOUNT_TRANSACTIONS_PATH = "/1/accounts/1/transactions";

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

  static void createTransaction(JUnit4CitrusTestDesigner testDesigner, String from, long basicBalance,
                                String to, long amount, String comment) {
    createUser(testDesigner, to);
    testDesigner.action(new AbstractTestAction() {
      @Override
      public void doExecute(TestContext testContext) {
        testContext.setVariable("toUserId", testContext.getVariable("userId"));
      }
    });

    createAccount(testDesigner);
    testDesigner.action(new AbstractTestAction() {
      @Override
      public void doExecute(TestContext testContext) {
        testContext.setVariable("toAccountId", testContext.getVariable("accountId"));
      }
    });

    createUser(testDesigner, from);
    createAccount(testDesigner);

    testDesigner.http().client(USERS_ENDPOINT)
        .send()
        .post(BANK_ACCOUNT_TRANSACTIONS_PATH)
        .payload(Json.createObjectBuilder()
            .add("toAccountId", "${accountId}")
            .add("amount", basicBalance)
            .add("comment", "Basic refill")
            .build()
            .toString());

    testDesigner.http().client(USERS_ENDPOINT)
        .send()
        .post(TRANSACTIONS_PATH)
        .payload(Json.createObjectBuilder()
            .add("toAccountId", "${toAccountId}")
            .add("amount", amount)
            .add("comment", comment)
            .build()
            .toString());

    testDesigner.http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.CREATED)
        .extractFromHeader("Location", "${location}");

    testDesigner.action(new AbstractTestAction() {
      @Override
      public void doExecute(TestContext testContext) {
        testContext.setVariable("transactionId", extractIdFromLocation(testContext));
      }
    });
  }
}
