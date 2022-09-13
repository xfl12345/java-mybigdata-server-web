package cc.xfl12345.mybigdata.server.web.model.api.result;

public class BooleanAndMessage {
    public boolean success;
    public String message;

    public BooleanAndMessage() {
    }

    public BooleanAndMessage(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public BooleanAndMessage(BooleanAndMessage booleanAndMessage) {
        setBooleanAndMessage(booleanAndMessage);
    }

    public void setBooleanAndMessage(BooleanAndMessage booleanAndMessage) {
        this.message = booleanAndMessage.message;
        this.success = booleanAndMessage.success;
    }
}
