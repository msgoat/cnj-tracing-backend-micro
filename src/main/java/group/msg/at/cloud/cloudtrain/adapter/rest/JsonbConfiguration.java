package group.msg.at.cloud.cloudtrain.adapter.rest;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

/**
 * Custom configuration of JSON-B.
 * <p>
 * Although JSON-B supports new Java stuff like the date-time API introduced with Java 8 out of
 * the box, support for common types like {@linkplain java.util.Locale} is missing. This
 * configuration class is supposed to close the gaps.
 * </p>
 */
@Provider
public class JsonbConfiguration implements ContextResolver<Jsonb> {

    private final Jsonb jsonb;

    public JsonbConfiguration() {
        JsonbConfig config = new JsonbConfig()
                .withFormatting(false)
                .withSerializers(new LocaleJsonSerializer())
                .withDeserializers(new LocaleJsonDeserializer());
        jsonb = JsonbBuilder.create(config);
    }

    @Override
    public Jsonb getContext(Class<?> type) {
        return jsonb;
    }

}