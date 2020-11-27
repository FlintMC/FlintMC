package net.flintmc.framework.config.internal.serialization.defaults;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.serialization.ConfigSerializationHandler;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.config.serialization.ConfigSerializer;

import java.util.ArrayList;
import java.util.Collection;

@Singleton
@ConfigSerializer(Collection.class)
public class CollectionConfigSerializer implements ConfigSerializationHandler<Collection<?>> {

  private final ConfigSerializationService serializationService;

  @Inject
  public CollectionConfigSerializer(ConfigSerializationService serializationService) {
    this.serializationService = serializationService;
  }

  @Override
  public JsonElement serialize(Collection<?> collection) {
    JsonArray array = new JsonArray();
    for (Object value : collection) {
      JsonElement element = this.serializationService.serializeWithType(value);
      if (element != null) {
        array.add(element);
      }
    }

    return array;
  }

  @Override
  public Collection<?> deserialize(JsonElement source) {
    if (!source.isJsonArray()) {
      return null;
    }
    JsonArray array = source.getAsJsonArray();
    Collection<Object> result = new ArrayList<>();

    for (JsonElement element : array) {
      Object value = this.serializationService.deserializeWithType(element);
      if (value != null) {
        result.add(value);
      }
    }

    return result;
  }
}
