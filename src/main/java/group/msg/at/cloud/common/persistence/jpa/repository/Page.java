package group.msg.at.cloud.common.persistence.jpa.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object that represents a page of results (for page-by-page
 * iteration).
 * <p>
 * This implementation is based on the ValueListHandler pattern as presented on
 * the CORE JavaEE Patterns website.
 * </p>
 *
 * @author Michael Theis (michael.theis@msg.group)
 * @version 1.0
 * @since release 1.0.0
 */
public class Page<E> implements Serializable, Comparable<Page<E>> {

    private static final long serialVersionUID = 170679486581687646L;

    /**
     * List of elements contained in this page.
     */
    private final List<E> elements;

    /**
     * Zero-based index of the first element in this page in the list of available
     * elements.
     */
    private final int firstPosition;

    /**
     * Total number of elements available in result set.
     */
    private final int numberOfElements;

    /**
     * Default constructor that creates an empty page.
     */
    public Page() {
        this.elements = new ArrayList<E>();
        this.firstPosition = 0;
        this.numberOfElements = 0;
    }

    /**
     * Constructs a page that contains the given page elements and points into a
     * result set containing the specified number of elements at the specified
     * position.
     */
    public Page(List<E> pageElements, int elementIndex, int numberOfElements) {
        this.elements = new ArrayList<E>(pageElements);
        this.firstPosition = elementIndex;
        this.numberOfElements = numberOfElements;
    }

    /**
     * Constructs a page that contains the given page elements and points into a
     * result set containing the specified number of elements at the specified
     * position.
     * <p>
     * Accepts <code>long</code> values as total number of elements since counting
     * queries return long values not int values. JPA only supports only int values
     * for firstResult and maxResult, so for sake of simplicity we internally stick
     * to int values for numberOfElements as well, assuming that traversing through
     * result sets with more than 2 million records does not make sense anyway.
     * </p>
     *
     * @throws IllegalArgumentException - if numberOfElements > Integer.MAX_VALUE
     */
    public Page(List<E> pageElements, int elementIndex, long numberOfElements) {
        if (numberOfElements > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Value of parameter numberOfElements [" + numberOfElements
                    + "] exceeds range of integer values!");
        }
        this.elements = new ArrayList<E>(pageElements);
        this.firstPosition = elementIndex;
        this.numberOfElements = (int) numberOfElements;
    }

    /**
     * Returns the list of elements contained in this page.
     *
     * @return list of elements; may be empty but never <code>null</code>.
     */
    public List<E> getElements() {
        return this.elements;
    }

    /**
     * Returns the total number of elements available in the result set.
     * <p>
     * Please note that implementations of repositories that support pagination are
     * required to initialize this property only if the first page at element index
     * 0 is retrieved.
     * </p>
     */
    public int getNumberOfElements() {
        return this.numberOfElements;
    }

    /**
     * Returns the position of the first element on this page related to the
     * underlying result set.
     */
    public int getFirstPosition() {
        return this.firstPosition;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.firstPosition;
        result = prime * result + this.numberOfElements;
        return result;
    }

    /**
     * @see Object#equals(Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Page)) {
            return false;
        }
        final Page<E> other = (Page<E>) obj;
        if (this.firstPosition != other.firstPosition) {
            return false;
        }
        return this.numberOfElements == other.numberOfElements;
    }

    /**
     * @see Comparable#compareTo(Object)
     */
    @Override
    public int compareTo(Page<E> other) {
        return this.firstPosition - other.firstPosition;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "Page { pageSize : " + this.elements.size() + ", firstPosition : " + this.firstPosition
                + ", numberOfElements : " + this.numberOfElements + " }";
    }
}
