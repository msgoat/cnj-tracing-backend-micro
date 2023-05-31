package group.msg.at.cloud.common.persistence.jpa.repository;

import jakarta.persistence.Query;

/**
 * Represents a set of query parameters that are supposed to be passed to a
 * {@link Query}.
 *
 * @author Michael Theis (michael.theis@msg.group)
 * @version 1.0
 * @since release 1.0.0
 */
public interface QueryParameters {
    /**
     * Applies all internally stored parameters to the given {@code Query}.
     *
     * @param query JPA Query
     */
    void applyParameters(Query query);
}