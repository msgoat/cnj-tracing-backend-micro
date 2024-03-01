package group.msg.at.cloud.cloudtrain.adapter.rest;

import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.stream.JsonParser;

import java.lang.reflect.Type;
import java.util.Locale;

/**
 * Custom JSON-B deserializer for type {@link Locale}.
 * <p>
 * Must be programmatically registered using a JSON-B configuration class.
 * </p>
 *
 * @see JsonbConfiguration
 */
public final class LocaleJsonDeserializer implements JsonbDeserializer<Locale> {

    @Override
    public Locale deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
        Locale result = null;
        if (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            if (event == JsonParser.Event.VALUE_STRING) {
                String text = parser.getString();
                result = Locale.forLanguageTag(text);
            }
        }
        return result;
    }
}
