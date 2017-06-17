package kae.demo.transferrest.api.it;

import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.message.MessageType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import javax.json.Json;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;

/**
 *
 */
public class UserResourceIT extends JUnit4CitrusTestDesigner {

  private static final String USERS_ENDPOINT = "users";

  private void createUser(String name) {
    http().client(USERS_ENDPOINT)
        .send()
        .post()
        .payload(Json.createObjectBuilder()
            .add("name", name)
            .build()
            .toString());

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NO_CONTENT)
        .extractFromHeader("Location", "${location}");

    action(new AbstractTestAction() {
      @Override
      public void doExecute(TestContext context) {
        final String location = context.getVariable("location");
        assertNotNull(location);
        final long id = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));
        context.setVariable("userId", id);
      }
    });
  }

  @Test
  @CitrusTest
  public void testCreateUser() throws Exception {
    createUser("Viktor Pelevin");
  }

  @Test
  @CitrusTest
  public void testGetUsers() throws Exception {
    createUser("Lev Tolstoy");

    http().client(USERS_ENDPOINT)
        .send()
        .get();

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.size()", greaterThan(0));
  }

  @Test
  @CitrusTest
  public void testGetUser() throws Exception {
    createUser("Fedor Dostoevsky");

    http().client(USERS_ENDPOINT)
        .send()
        .get("/${userId}");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.id", "${userId}")
        .jsonPath("$.name", "Fedor Dostoevsky");
  }

  @Test
  @CitrusTest
  public void testUpdateUser() throws Exception {
    http().client(USERS_ENDPOINT)
        .send()
        .put("/3")
        .payload(Json.createObjectBuilder()
            .add("name", "Kurt Vonnegut")
            .build()
            .toString());

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NO_CONTENT);
  }

}
