package group.msg.at.cloud.common.persistence.jpa.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.UUID;

/**
 * {@code JPA Attribute Converter} to have support for UUID attribute and
 * PostgreSQL UUID columns.
 * <p>
 * Actually, the implementation looks a little awkward (same java type and db
 * type) but this is the only way to convince the PostgreSQL JDBC driver to map
 * {@code UUID} attribute values to {@code UUID} db column values.
 * </p>
 *
 * @author Michael Theis (michael.theis@msg.group)
 * @version 1.0
 * @since release 1.0.0
 */
@Converter(autoApply = true)
public class UuidConverter implements AttributeConverter<UUID, Object> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Object convertToDatabaseColumn(UUID attribute) {
        PostgresUuidObject result = new PostgresUuidObject(attribute);
        log.debug("Converting [{}] to [{}]", attribute, result);
        return result;
    }

    @Override
    public UUID convertToEntityAttribute(Object dbData) {
        UUID result = null;
        if (dbData != null) {
            if (dbData instanceof UUID) {
                result = (UUID) dbData;
            } else if (dbData instanceof String) {
                result = UUID.fromString((String) dbData);
            }
        }
        log.debug("Converting [{}] to [{}]", dbData, result);
        return result;
    }

}
