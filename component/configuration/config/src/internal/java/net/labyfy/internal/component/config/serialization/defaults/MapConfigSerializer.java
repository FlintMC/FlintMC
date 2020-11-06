package net.labyfy.internal.component.config.serialization.defaults;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.serialization.ConfigSerializationHandler;
import net.labyfy.component.config.serialization.ConfigSerializer;

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
