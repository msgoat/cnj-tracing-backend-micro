package group.msg.at.cloud.common.persistence.jpa.audit;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.security.Principal;
import java.time.LocalDateTime;

/**
 * {@code JPA lifecycle listener} that tracks creation and modification of
 * {@link AbstractAuditableEntity}s.
 * <p>
 * <strong>Note:</strong> Due to a known bug in Payara's version of EclipseLink, EclipseLink does
 * not support CDI in JPA resources as long as AttributeConverters are present. So we have to
 * use a programmatic lookup of the {@code SecurityContext} if the local field should be {@code null}.
 * </p>
 *
 * @author Michael Theis (michael.theis@msg.group)
 * @version 1.1
 * @see https://github.com/payara/Payara/issues/3720
 * @since release 1.0.0
 */
public class AuditableEntityListener {

    @Inject
    private Principal securityContext;

    @PrePersist
    public void onPrePersist(AbstractAuditableEntity entity) {
        entity.trackCreation(getAuthenticatedUserId(), LocalDateTime.now());
    }

    @PreUpdate
    public void onPreUpdate(AbstractAuditableEntity entity) {
        entity.trackModification(getAuthenticatedUserId(), LocalDateTime.now());
    }

    private String getAuthenticatedUserId() {
        String result = "anonymous";
        if (getSecurityContext() != null) {
            result = getSecurityContext().getName();
        }
        return result;
    }

    /**
     * Wraps access to {@link #securityContext} in order to perform a programmatic CDI lookup in case
     * CDI injection didn't happen.
     */
    private Principal getSecurityContext() {
        if (this.securityContext == null) {
            this.securityContext = CDI.current().select(Principal.class).get();
        }
        return this.securityContext;
    }

}
