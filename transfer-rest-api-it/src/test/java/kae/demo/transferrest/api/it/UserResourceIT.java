package kae.demo.transferrest.api.it;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.message.MessageType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

/**
 *
 */
public class UserResourceIT extends JUnit4CitrusTestDesigner {

  private static final String USERS_ENDPOINT = "users";

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

}
