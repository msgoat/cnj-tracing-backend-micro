package group.msg.at.cloud.common.persistence.jpa.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Common base class for all {@code Entities} that are supposed to be auditable.
 * <p>
 * The following audit information will be retained:
 * </p>
 * <ul>
 * <li><b>createdBy</b>: user ID of the user that created this entity</li>
 * <li><b>createdAt</b>: date/time this entity was created</li>
 * <li><b>lastModifiedBy</b>: user ID of the user that modified this entity the
 * last time</li>
 * <li><b>lastModification</b>: date/time this entity was modified the last time
 * </li>
 * </ul>
 *
 * @author Michael Theis (michael.theis@msg.group)
 * @version 1.0
 * @since release 1.0.0
 */
@MappedSuperclass
@EntityListeners({AuditableEntityListener.class})
public class AbstractAuditableEntity {

    @Size(max = 31)
    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Size(max = 31)
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @Column(name = "LAST_MODIFIED_AT")
    private LocalDateTime lastModifiedAt;

    /**
     * Returns the user ID of the user that created this entity.
     */
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Returns the creation date and time of this entity.
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Tells this entity to retain the specified user ID and point in time as the
     * createdBy and the createdAt of this entity.
     * <p>
     * Usually this method is called by {@code Repository} implementations that
     * support auditable entities.
     * </p>
     */
    void trackCreation(String creatorId, LocalDateTime creationDate) {
        if (this.createdBy == null) {
            this.createdBy = creatorId;
        }
        if (this.createdAt == null) {
            this.createdAt = creationDate;
        }
        if (this.lastModifiedBy == null) {
            this.lastModifiedBy = creatorId;
        }
        if (this.lastModifiedAt == null) {
            this.lastModifiedAt = creationDate;
        }
    }

    /**
     * Returns the user ID of the user that modified this entity the last time.
     */
    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    /**
     * Returns the date and time of the last modification of this entity.
     */
    public LocalDateTime getLastModifiedAt() {
        return this.lastModifiedAt;
    }

    /**
     * Tells this entity to retain the specified user ID and point in time as the
     * lastModifiedBy and the lastModifiedAt of this entity.
     * <p>
     * Usually this method is called by {@code Repository} implementations that
     * support auditable entities.
     * </p>
     */
    void trackModification(String lastModifierId, LocalDateTime lastModificationDate) {
        this.lastModifiedBy = lastModifierId;
        this.lastModifiedAt = lastModificationDate;
    }

    /**
     * Supports tracking of custom audit information which is supposed to override
     * the automatic audit information.
     * <p>
     * The given creation information overrides the current creation information, if
     * there is no creation information available or if the given creation date is
     * before the current creation date.
     * </p>
     * <p>
     * The given modification information overrides the current modification
     * information, if there is no modification information available or if the
     * given modification date is after the current modification date.
     * </p>
     */
    protected final void trackCustomAuditInformation(String creatorId, LocalDateTime creationDate,
                                                     String lastModifierId, LocalDateTime lastModificationDate) {
        Objects.requireNonNull(creatorId, "Missing required parameter creatorId!");
        Objects.requireNonNull(creationDate, "Missing required parameter creationDate!");
        Objects.requireNonNull(lastModifierId, "Missing required parameter lastModifierId!");
        Objects.requireNonNull(lastModificationDate, "Missing required parameter lastModificationDate!");
        if (this.createdAt == null || creationDate.isBefore(this.createdAt)) {
            this.createdBy = creatorId;
            this.createdAt = creationDate;
        }
        if (this.lastModifiedAt == null || lastModificationDate.isAfter(this.lastModifiedAt)) {
            this.lastModifiedBy = lastModifierId;
            this.lastModifiedAt = lastModificationDate;
        }
    }
}
