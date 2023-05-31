package group.msg.at.cloud.common.persistence.jpa.repository;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * Simple DTO that holds name and value of query parameters.
 *
 * @author Michael Theis (michael.theis@msg.group)
 * @version 1.0
 * @since release 1.0.0
 */
public final class QueryParameter implements Serializable {

    private static final long serialVersionUID = -2044905704670188349L;

    @NotNull
    private String name;

    @NotNull
    private Object value;

    public QueryParameter() {
    }

    public QueryParameter(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name of the parameter as defined in a named query.
     *
     * @return parameter name; never {@code null}
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the value of the parameter to be used in a named query.
     *
     * @return parameter value; never {@code null}
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Takes only the parameter name into account when checking for equality since
     * parameter passed to queries must have unique names.
     *
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        boolean result = this == o;
        if (!result && o instanceof QueryParameter) {
            final QueryParameter rhs = (QueryParameter) o;
            result = this.name.equals(rhs.name);
        }
        return result;
    }

    /**
     * Takes only the parameter name into account when calculating the hashcode
     * since parameter passed to queries must have unique names.
     *
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = result * 37 + this.name.hashCode();
        return result;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuilder().append("QueryParameter{").append("name=").append(this.name).append("}").toString();
    }
}
