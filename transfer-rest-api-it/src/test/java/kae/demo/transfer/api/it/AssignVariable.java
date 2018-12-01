package kae.demo.transfer.api.it;

import com.consol.citrus.actions.AbstractTestAction;
import com.consol.citrus.context.TestContext;

/** */
public class AssignVariable extends AbstractTestAction {

  private final String name;
  private final String nameFrom;

  public AssignVariable(String name, String nameFrom) {
    this.name = name;
    this.nameFrom = nameFrom;
  }

  @Override
  public void doExecute(TestContext testContext) {
    testContext.setVariable(name, testContext.getVariable(nameFrom));
  }
}
