package group.msg.at.cloud.common.persistence.jpa.repository;

import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Container für indizierte SQL-Parameter.
 *
 * @author theism
 * @version 1.0
 * @since R2016.1 10.08.2015
 */
public final class IndexedQueryParameters implements QueryParameters {
    /**
     * Alle zu übergebenden SQL-Parameter
     */
    private final List<Object> parameters;

    /**
     * Erzeugt eine vollständig initialisierte Instanz.
     *
     * @param parameters alle zu übergebenden SQL-Parameter
     */
    private IndexedQueryParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * @see QueryParameters#applyParameters(Query)
     */
    @Override
    public void applyParameters(Query query) {
        int parameterIndex = 1;
        for (Object current : this.parameters) {
            query.setParameter(parameterIndex, current);
            parameterIndex++;
        }
    }

    /**
     * Builder für {@code IndexedQueryParameters}.
     */
    public static final class Builder {
        /**
         * Map mit allen benannten Parametern.
         */
        private final List<Object> parameters = new ArrayList<Object>();

        /**
         * Übernimmt den angegebenen SQL-Parameter, wobei der Parameter einfach
         * an das Ende der Liste der bisher übergebenen Parameter angehängt wird.
         * Die Position in der Liste muss dem Index des Parameters im später
         * auszuführenden SQL-Statement entsprechen.
         *
         * @param value
         * @return {@code this} für Konkatenation von Methoden-Aufrufen (Fluid API)
         */
        public Builder withParameter(Object value) {
            this.parameters.add(value);
            return this;
        }

        /**
         * Erstellt eine Instanz vom Typ {@code IndexedQueryParameters} mit allen zuvor angegebenen SQL-Parametern.
         *
         * @return {@code IndexedQueryParameters}
         */
        public QueryParameters build() {
            return new IndexedQueryParameters(parameters);
        }
    }
}
