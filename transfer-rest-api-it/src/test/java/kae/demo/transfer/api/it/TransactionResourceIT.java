package kae.demo.transfer.api.it;

import static kae.demo.transfer.api.it.AccountSeeder.createAccount;
import static kae.demo.transfer.api.it.Endpoints.ACCOUNTS_PATH;
import static kae.demo.transfer.api.it.Endpoints.BANK_ACCOUNT_TRANSACTIONS_PATH;
import static kae.demo.transfer.api.it.Endpoints.TRANSACTIONS_PATH;
import static kae.demo.transfer.api.it.Endpoints.TRANSACTION_PATH;
import static kae.demo.transfer.api.it.Endpoints.USERS_ENDPOINT;
import static kae.demo.transfer.api.it.TransactionSeeder.createTransaction;
import static kae.demo.transfer.api.it.UserSeeder.createUser;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.message.MessageType;
import javax.json.Json;
import org.junit.Test;
import org.springframework.http.HttpStatus;

/** */
public class TransactionResourceIT extends JUnit4CitrusTestDesigner {

  @Test
  @CitrusTest
  public void testCreateTransaction() {
    final long basicBalance = 100_000_000_000L;
    final long amount = 5_000_000_000L;
    createTransaction(
        this, "Alisher Usmanov", basicBalance, "Dmitry Medvedev", amount, "This is not a bribe");

    http().client(USERS_ENDPOINT).send().get(ACCOUNTS_PATH + "/${accountId}");

    http()
        .client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.id", "${accountId}")
        .jsonPath("$.userId", "${userId}")
        .jsonPath("$.balance", basicBalance - amount);

    http().client(USERS_ENDPOINT).send().get("/${toUserId}/accounts/${toAccountId}");

    http()
        .client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.id", "${toAccountId}")
        .jsonPath("$.userId", "${toUserId}")
        .jsonPath("$.balance", amount);
  }

  @Test
  @CitrusTest
  public void testCreateTransactionWithNotEnoughFunds() {
    createUser(this, "Irish Pub");
    action(
        new AbstractTestAction() {
          @Override
          public void doExecute(TestContext testContext) {
            testContext.setVariable("toUserId", testContext.getVariable("userId"));
          }
        });

    createAccount(this);
    action(
        new AbstractTestAction() {
          @Override
          public void doExecute(TestContext testContext) {
            testContext.setVariable("toAccountId", testContext.getVariable("accountId"));
          }
        });

    createUser(this, "Anton Kapralov");
    createAccount(this);

    http()
        .client(USERS_ENDPOINT)
        .send()
        .post(BANK_ACCOUNT_TRANSACTIONS_PATH)
        .payload(
            Json.createObjectBuilder()
                .add("toAccountId", "${accountId}")
                .add("amount", 100L)
                .add("comment", "Basic refill")
                .build()
                .toString());

    http()
        .client(USERS_ENDPOINT)
        .send()
        .post(TRANSACTIONS_PATH)
        .payload(
            Json.createObjectBuilder()
                .add("toAccountId", "${toAccountId}")
                .add("amount", 250L)
                .add("comment", "Guinness")
                .build()
                .toString());

    http()
        .client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.BAD_REQUEST)
        .jsonPath("$.message", "Not enough funds");
  }

  @Test
  @CitrusTest
  public void testGetTransactions() {
    createTransaction(
        this, "Mark Zuckerberg", 100L, "Pavel Durov", 100L, "Thanks for beer!");

    http().client(USERS_ENDPOINT).send().get(TRANSACTIONS_PATH);

    http()
        .client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.size()", greaterThan(0));
  }

  @Test
  @CitrusTest
  public void testGetTransaction() {
    final long amount = 300L;
    final String comment = "For Catch-22";
    createTransaction(this, "Kurt Vonnegut", amount, "Joseph Heller", amount, comment);

    http()
        .client(USERS_ENDPOINT)
        .send()
        .get(TRANSACTION_PATH);

    http()
        .client(USERS_ENDPOINT)
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
  public void testDeleteTransaction() {
    createTransaction(
        this, "Anonymous", 1000L, "Alexey Navalny", 1000L, "For the great justice!");

    http()
        .client(USERS_ENDPOINT)
        .send()
        .delete(TRANSACTION_PATH);

    http().client(USERS_ENDPOINT).receive().response(HttpStatus.NO_CONTENT);

    http()
        .client(USERS_ENDPOINT)
        .send()
        .get(TRANSACTION_PATH);

    http()
        .client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NOT_FOUND)
        .messageType(MessageType.JSON);
  }
}
