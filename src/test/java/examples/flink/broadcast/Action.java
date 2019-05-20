package examples.flink.broadcast;

public class Action {

    public Action() {}

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

    private Long userId;
    private ActionType action;
}
