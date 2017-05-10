package org.serge.ws.rs.hk2.event;

public class TextEvent {

    private String message;

    public TextEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
