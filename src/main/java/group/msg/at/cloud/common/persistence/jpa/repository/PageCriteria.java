package group.msg.at.cloud.common.persistence.jpa.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bundles attributes that control the contents of paginated queries.
 * <p>
 * Right now, this value class only supports pagination as supported by JPA
 * using a {@code firstPosition} which defines the first element on the
 * resulting page and {@code pageSize} which determines the maximum number of
 * elements on the resulting page. Nevertheless, introducing a dedicated value
 * class for page criteria provides an excellent extension point for custom
 * pagination algorithms.
 * </p>
 *
 * @author Michael Theis (michael.theis@msg.group)
 * @version 1.0
 * @since release 1.0.0
 */
public final class PageCriteria implements Serializable {

    private static final long serialVersionUID = -5051476904316260824L;

    private List<QueryParameter> queryParameters = Collections.emptyList();

    private int firstPosition;

    private int pageSize;

    public PageCriteria() {
    }

    /**
     * Specialized constructor which provides a fully initialized instance.
     *
     * @param queryParameters query parameters to be passed to both queries
     * @param firstPosition   zero-based index of the first element on a page
     * @param pageSize        maximum number of elements on a page
     */
    public PageCriteria(List<QueryParameter> queryParameters, int firstPosition, int pageSize) {
        if (queryParameters != null && !queryParameters.isEmpty()) {
            this.queryParameters = new ArrayList<QueryParameter>(queryParameters);
        }
        this.firstPosition = firstPosition;
        this.pageSize = pageSize;
    }

    /**
     * Returns the unmodifiable list of {@code QueryParameter}s to be passed to the
     * query.
     */
    public List<QueryParameter> getQueryParameters() {
        return this.queryParameters;
    }

    /**
     * Returns the zero-based index of the first element to copied from the query
     * result set.
     */
    public int getFirstPosition() {
        return this.firstPosition;
    }

    /**
     * Returns the number of elements that one page can hold.
     */
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.queryParameters.hashCode();
        result = prime * result + this.firstPosition;
        result = prime * result + this.pageSize;
        return result;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PageCriteria other = (PageCriteria) obj;
        if (!this.queryParameters.equals(other.queryParameters)) {
            return false;
        }
        if (this.firstPosition != other.firstPosition) {
            return false;
        }
        return this.pageSize == other.pageSize;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("PageCriteria { queryParameters : [ ");
        for (final QueryParameter current : this.queryParameters) {
            result.append(current).append(" ");
        }
        result.append("], firstPosition : ").append(this.firstPosition).append(", pageSize : ").append(this.pageSize)
                .append(" }");
        return result.toString();
    }
}
