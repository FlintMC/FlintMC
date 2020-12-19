package net.flintmc.framework.config.internal.serialization.defaults;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.serialization.ConfigSerializationHandler;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.config.serialization.ConfigSerializer;

import java.util.HashMap;
import java.util.Map;

@Singleton
@ConfigSerializer(Map.class)
public class MapConfigSerializer implements ConfigSerializationHandler<Map<?, ?>> {

  private final ConfigSerializationService serializationService;

  @Inject
  private MapConfigSerializer(ConfigSerializationService serializationService) {
    this.serializationService = serializationService;
  }

  /** {@inheritDoc} */
  @Override
  public JsonElement serialize(Map<?, ?> map) {
    JsonObject object = new JsonObject();

    for (Map.Entry<?, ?> entry : map.entrySet()) {
      String key = String.valueOf(entry.getKey());
      Object value = entry.getValue();

      JsonElement element = this.serializationService.serializeWithType(value);

      if (element != null) {
        object.add(key, element);
      }
    }

    return object;
  }

  /** {@inheritDoc} */
  @Override
  public Map<?, ?> deserialize(JsonElement source) {
    if (!source.isJsonObject()) {
      return null;
    }
    JsonObject object = source.getAsJsonObject();
    Map<String, Object> result = new HashMap<>();

    for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
      Object value = this.serializationService.deserializeWithType(entry.getValue());
      if (value != null) {
        result.put(entry.getKey(), value);
      }
    }

    return result;
  }
}
