package de.viadee.camunda.kafka.event;

import java.util.Date;

/**
 * <p>
 * IdentityLinkEvent class.
 * </p>
 *
 *
 * @version $Id: $Id
 */
public class IdentityLinkEvent extends DetailEvent {

    public enum OperationType {
        add, delete
    }

    private String type;
    private String userId;
    private String groupId;
    private OperationType operationType;
    private String assignerId;
    private Date removalTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getAssignerId() {
        return assignerId;
    }

    public void setAssignerId(String assignerId) {
        this.assignerId = assignerId;
    }

    public Date getRemovalTime() {
        return removalTime;
    }

    public void setRemovalTime(Date removalTime) {
        this.removalTime = removalTime;
    }
}
