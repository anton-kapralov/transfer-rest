package kae.demo.transfer.api.it;

import static org.junit.Assert.assertNotNull;

import com.consol.citrus.context.TestContext;

/** */
class IdExtractor {

  static long extractIdFromLocation(TestContext context) {
    final String location = context.getVariable("location");
    assertNotNull(location);
    return Long.valueOf(location.substring(location.lastIndexOf("/") + 1));
  }
}
