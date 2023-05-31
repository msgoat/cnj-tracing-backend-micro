package group.msg.at.cloud.common.persistence.jpa.convert;

import jakarta.persistence.PersistenceException;
import org.postgresql.util.PGobject;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public final class PostgresUuidObject extends PGobject implements Comparable<Object> {

    PostgresUuidObject(UUID value) {
        setType("uuid");
        try {
            setValue(value != null ? value.toString() : null);
        } catch (SQLException e) {
            throw new PersistenceException(String.format("Unable to set value [%s]", value), e);
        }
    }

    @Override
    public int compareTo(Object o) {
        Objects.requireNonNull(o, "Given value must not be null!");
        int result = 0;
        if (getValue() != null) {
            if (o instanceof UUID) {
                result = getValue().compareTo(((UUID) o).toString());
            }
        } else {
            result = -1;
        }
        return result;
    }
}
