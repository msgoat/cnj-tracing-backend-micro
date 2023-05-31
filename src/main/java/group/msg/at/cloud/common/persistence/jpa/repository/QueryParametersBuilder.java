package group.msg.at.cloud.common.persistence.jpa.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code Builder} implementation that helps to create lists of
 * {@code QueryParameter}s.
 *
 * @author Michael Theis (michael.theis@msg.group)
 * @version 1.0
 * @since release 1.0.0
 */
public final class QueryParametersBuilder {

    private final List<QueryParameter> parameters;

    /**
     * Constructs a new builder instance creating a parameter list using an initial
     * default size.
     */
    public QueryParametersBuilder() {
        this.parameters = new ArrayList<QueryParameter>();
    }

    /**
     * Constructs a new builder instance starting with a parameter list that is
     * large enough to hold the given number of parameters.
     * <p>
     * Of course, the parameter list is capable of holding more parameters than
     * specified here.
     * </p>
     *
     * @param numberOfParameters expected number of parameters
     */
    public QueryParametersBuilder(int numberOfParameters) {
        this.parameters = new ArrayList<QueryParameter>(numberOfParameters);
    }

    /**
     * Adds the parameter with the given name and value to the parameter list.
     *
     * @param name  parameter name; must not be empty.
     * @param value parameter value; must not be {@code null}
     * @return this builder instance to provide a fluent API.
     */
    public QueryParametersBuilder withParameter(String name, Object value) {
        this.parameters.add(new QueryParameter(name, value));
        return this;
    }

    /**
     * Returns the unmodifiable parameter list.
     */
    public List<QueryParameter> build() {
        return Collections.unmodifiableList(this.parameters);
    }
}
