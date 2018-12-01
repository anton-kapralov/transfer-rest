package kae.demo.transfer.api.it;

import static kae.demo.transfer.api.it.Endpoints.BANK_ACCOUNT_TRANSACTIONS_PATH;
import static kae.demo.transfer.api.it.Endpoints.TRANSACTIONS_PATH;
import static kae.demo.transfer.api.it.Endpoints.USERS_ENDPOINT;

import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import javax.json.Json;
import org.springframework.http.HttpStatus;

/** */
class TransactionSeeder {

  private TransactionSeeder() {}

  static void createTransaction(
      JUnit4CitrusTestDesigner testDesigner,
      String from,
      long basicBalance,
      String to,
      long amount,
      String comment) {
    UserSeeder.createUser(testDesigner, to);
    testDesigner.action(new AssignVariable("toUserId", "userId"));

    AccountSeeder.createAccount(testDesigner);
    testDesigner.action(new AssignVariable("toAccountId", "accountId"));

    UserSeeder.createUser(testDesigner, from);
    AccountSeeder.createAccount(testDesigner);

    testDesigner
        .http()
        .client(USERS_ENDPOINT)
        .send()
        .post(BANK_ACCOUNT_TRANSACTIONS_PATH)
        .payload(
            Json.createObjectBuilder()
                .add("toAccountId", "${accountId}")
                .add("amount", basicBalance)
                .add("comment", "Basic refill")
                .build()
                .toString());

    testDesigner
        .http()
        .client(USERS_ENDPOINT)
        .send()
        .post(TRANSACTIONS_PATH)
        .payload(
            Json.createObjectBuilder()
                .add("toAccountId", "${toAccountId}")
                .add("amount", amount)
                .add("comment", comment)
                .build()
                .toString());

    testDesigner
        .http()
        .client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.CREATED)
        .extractFromHeader("Location", "${location}");

    testDesigner.action(new SaveIdFromLocationTo("transactionId"));
  }
}
