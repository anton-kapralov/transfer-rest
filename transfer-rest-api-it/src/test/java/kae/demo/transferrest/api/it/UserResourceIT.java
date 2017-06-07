package kae.demo.transferrest.api.it;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.message.MessageType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import javax.json.Json;

/**
 *
 */
public class UserResourceIT extends JUnit4CitrusTestDesigner {

  private static final String USERS_ENDPOINT = "users";

  @Test
  @CitrusTest
  public void testCreateUser() throws Exception {
    http().client(USERS_ENDPOINT)
        .send()
        .post()
        .payload(Json.createObjectBuilder()
            .add("name", "Viktor Pelevin")
            .build()
            .toString());

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.NO_CONTENT);
  }

  @Test
  @CitrusTest
  public void testGetUsers() throws Exception {
    http().client(USERS_ENDPOINT)
        .send()
        .get();

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.[0].id", 1)
        .jsonPath("$.[0].name", "Lev Tolstoy");
  }

  @Test
  @CitrusTest
  public void testGetUser() throws Exception {
    http().client(USERS_ENDPOINT)
        .send()
        .get("/2");

    http().client(USERS_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.JSON)
        .jsonPath("$.id", 2)
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
