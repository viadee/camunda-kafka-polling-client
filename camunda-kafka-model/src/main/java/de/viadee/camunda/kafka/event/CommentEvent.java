package de.viadee.camunda.kafka.event;

/**
 * <p>
 * CommentEvent class.
 * </p>
 *
 *
 * @version $Id: $Id
 */
public class CommentEvent extends DetailEvent {

    private String message;
    private String userId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
