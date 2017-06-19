package kae.demo.transferrest.api.it;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.message.MessageType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import javax.json.Json;

import static kae.demo.transferrest.api.it.ITHelper.USERS_ENDPOINT;
import static kae.demo.transferrest.api.it.ITHelper.createUser;
import static org.hamcrest.Matchers.greaterThan;

/**
 *
 */
public class UserResourceIT extends JUnit4CitrusTestDesigner {

  @Test
  @CitrusTest
  public void testCreateUser() throws Exception {
    createUser(this, "Viktor Pelevin");
  }

  @Test
  @CitrusTest
  public void testGetUsers() throws Exception {
    createUser(this, "Lev Tolstoy");

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
    createUser(this, "Fedor Dostoevsky");

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
    createUser(this, "Laurence Wachowski");

    final String newName = "Lana Wachowski";
    http().client(USERS_ENDPOINT)
        .send()
        .put("/${userId}")
        .payload(Json.createObjectBuilder()
            .add("name", newName)
            .build()
            .toString());

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NO_CONTENT);

    http().client(USERS_ENDPOINT)
        .send()
        .get("/${userId}");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.id", "${userId}")
        .jsonPath("$.name", newName);
  }

  @Test
  @CitrusTest
  public void testDeleteUser() throws Exception {
    createUser(this, "Anton Kapralov");

    http().client(USERS_ENDPOINT)
        .send()
        .delete("/${userId}");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NO_CONTENT);

    http().client(USERS_ENDPOINT)
        .send()
        .get("/${userId}");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NOT_FOUND);
  }

  @Test
  @CitrusTest
  public void testDeleteUnknownUser() throws Exception {
    http().client(USERS_ENDPOINT)
        .send()
        .delete("/" + Integer.MAX_VALUE);

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NOT_FOUND);
  }

}
