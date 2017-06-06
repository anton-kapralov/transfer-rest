package kae.demo.transferrest.api.it;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.message.MessageType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

/**
 *
 */
public class HealthResourceIT extends JUnit4CitrusTestDesigner {

  private static final String HEALTH_ENDPOINT = "health";

  @Test
  @CitrusTest
  public void testGetHealth() throws Exception {
    http().client(HEALTH_ENDPOINT)
        .send()
        .get();

    http().client(HEALTH_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.PLAINTEXT)
        .payload("OK");
  }

}