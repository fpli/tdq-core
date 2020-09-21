package com.ebay.sojourner.rt.broadcast;

public class Action {

  private Long userId;
  private ActionType action;

  public Action() {
  }

  public Action(Long userId, ActionType action) {
    this.userId = userId;
    this.action = action;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public ActionType getAction() {
    return action;
  }

  public void setAction(ActionType action) {
    this.action = action;
  }
}
