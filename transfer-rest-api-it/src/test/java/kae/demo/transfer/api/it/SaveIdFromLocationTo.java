package kae.demo.transfer.api.it;

import static org.junit.Assert.assertNotNull;

import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.context.TestContext;

/** */
class SaveIdFromLocationTo extends AbstractTestAction {

  private String variableName;

  SaveIdFromLocationTo(String variableName) {
    this.variableName = variableName;
  }

  private static long extractIdFromLocation(TestContext context) {
    final String location = context.getVariable("location");
    assertNotNull(location);
    return Long.valueOf(location.substring(location.lastIndexOf("/") + 1));
  }

  @Override
  public void doExecute(TestContext testContext) {
    testContext.setVariable(variableName, extractIdFromLocation(testContext));
  }
}
