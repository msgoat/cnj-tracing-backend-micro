package group.msg.at.cloud.common.persistence.jpa.repository;

import jakarta.persistence.Query;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Konkrete Implementierung von {@link QueryParameters}, die benannte Parameter
 * unterstützt.
 *
 * @author Michael Theis (michael.theis@msg.group)
 * @version 1.0
 * @since release 1.0.0
 */
public final class NamedQueryParameters implements QueryParameters {
    private final Map<String, Object> parametersByName;

    private NamedQueryParameters(Map<String, Object> parametersByName) {
        this.parametersByName = parametersByName;
    }

    @Override
    public void applyParameters(Query query) {
        for (final Map.Entry<String, Object> current : this.parametersByName.entrySet()) {
            query.setParameter(current.getKey(), current.getValue());
        }
    }

    public static final class Builder {
        private final Map<String, Object> parametersByName = new LinkedHashMap<String, Object>();

        public Builder withParameter(String name, Object value) {
            this.parametersByName.put(name, value);
            return this;
        }

        public QueryParameters build() {
            return new NamedQueryParameters(this.parametersByName);
        }
    }
}
