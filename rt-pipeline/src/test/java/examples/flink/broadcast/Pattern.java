package examples.flink.broadcast;

public class Pattern {

  private ActionType firstAction;
  private ActionType secondAction;

  public Pattern() {
  }

  public Pattern(ActionType firstAction, ActionType secondAction) {
    this.firstAction = firstAction;
    this.secondAction = secondAction;
  }

  public ActionType getFirstAction() {
    return firstAction;
  }

  public void setFirstAction(ActionType firstAction) {
    this.firstAction = firstAction;
  }

  public ActionType getSecondAction() {
    return secondAction;
  }

  public void setSecondAction(ActionType secondAction) {
    this.secondAction = secondAction;
  }
}
