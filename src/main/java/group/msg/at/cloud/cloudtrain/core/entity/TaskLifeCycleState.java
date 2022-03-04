package group.msg.at.cloud.cloudtrain.core.entity;

/**
 * Enumeration representing the life-cycle of a {@link Task}.
 *
 * @author Michael Theis (mtheis@msg.group)
 * @version 1.0
 * @since release 1.0 31.10.2012 11:39:50
 */
public enum TaskLifeCycleState {
    /**
     * Status of the current task is undefined.
     */
    UNDEFINED,
    /**
     * Task is open but the submitter is still working on it.
     * <p>
     * Next statuses:
     * </p>
     * <ul>
     * <li>OPEN_RUNNING, if the requester submits the task</li>
     * </ul>
     */
    OPEN_UNDER_WORK,
    /**
     * Task is open and running, waiting for its completion by a responsible.
     * <p>
     * Next statuses:
     * </p>
     * <ul>
     * <li>CLOSED_REVOKED, if the requester revokes the task before its completion</li>
     * <li>CLOSED_CANCELLED, if the task is cancelled by a responsible</li>
     * </ul>
     */
    OPEN_RUNNING,
    /**
     * Task is closed and successfully completed.
     */
    CLOSED_COMPLETED,
    /**
     * Task is closed because the requester revoked it before its completion.
     */
    CLOSED_REVOKED,
    /**
     * Task is closed and has been cancelled by cancelled; associated service is not deployed.
     */
    CLOSED_CANCELLED
}
