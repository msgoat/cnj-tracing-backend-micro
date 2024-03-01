package group.msg.at.cloud.cloudtrain.adapter.rest;

import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;

import java.util.Locale;

/**
 * Custom JSON-B serializer for type {@link Locale}.
 * <p>
 * Must be programmatically registered using a JSON-B configuration class.
 * </p>
 *
 * @see JsonbConfiguration
 */
public final class LocaleJsonSerializer implements JsonbSerializer<Locale> {
    @Override
    public void serialize(Locale obj, JsonGenerator generator, SerializationContext ctx) {
        ctx.serialize(obj != null ? obj.toLanguageTag() : null, generator);
    }
}
