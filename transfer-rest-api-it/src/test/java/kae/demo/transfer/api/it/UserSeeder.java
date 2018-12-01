package kae.demo.transfer.api.it;

import static kae.demo.transfer.api.it.Endpoints.USERS_ENDPOINT;
import static kae.demo.transfer.api.it.IdExtractor.extractIdFromLocation;

import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import javax.json.Json;
import org.springframework.http.HttpStatus;

/** */
class UserSeeder {

  private UserSeeder() {}

  static void createUser(JUnit4CitrusTestDesigner testDesigner, String name) {
    testDesigner
        .http()
        .client(USERS_ENDPOINT)
        .send()
        .post()
        .payload(Json.createObjectBuilder().add("name", name).build().toString());

    testDesigner
        .http()
        .client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.CREATED)
        .extractFromHeader("Location", "${location}");

    testDesigner.action(
        new AbstractTestAction() {
          @Override
          public void doExecute(TestContext context) {
            context.setVariable("userId", extractIdFromLocation(context));
          }
        });
  }
}
