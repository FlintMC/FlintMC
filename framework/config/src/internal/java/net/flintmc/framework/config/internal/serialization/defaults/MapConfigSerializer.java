package net.flintmc.framework.config.internal.serialization.defaults;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.serialization.ConfigSerializationHandler;
import net.flintmc.framework.config.serialization.ConfigSerializer;

import java.util.Map;

@Singleton
@ConfigSerializer(Map.class)
public class MapConfigSerializer implements ConfigSerializationHandler<Map<?, ?>> {

  private final Gson gson;

  @Inject
  public MapConfigSerializer() {
    this.gson = new Gson();
  }

  @Override
  public JsonElement serialize(Map<?, ?> map) {
    return this.gson.toJsonTree(map);
  }

  @Override
  public Map<?, ?> deserialize(JsonElement element) {
    return this.gson.fromJson(element, Map.class);
  }
}
