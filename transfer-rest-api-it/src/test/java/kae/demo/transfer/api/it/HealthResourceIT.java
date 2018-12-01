package kae.demo.transfer.api.it;

import static kae.demo.transfer.api.it.Endpoints.HEALTH_ENDPOINT;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.message.MessageType;
import org.junit.Test;
import org.springframework.http.HttpStatus;

/** */
public class HealthResourceIT extends JUnit4CitrusTestDesigner {

  @Test
  @CitrusTest
  public void testGetHealth() {
    http().client(HEALTH_ENDPOINT).send().get();

    http()
        .client(HEALTH_ENDPOINT)
        .receive()
        .response(HttpStatus.OK)
        .messageType(MessageType.PLAINTEXT)
        .payload("OK");
  }
}
