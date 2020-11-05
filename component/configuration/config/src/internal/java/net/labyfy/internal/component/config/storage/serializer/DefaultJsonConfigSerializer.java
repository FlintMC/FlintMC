package net.labyfy.internal.component.config.storage.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.config.storage.serializer.JsonConfigSerializer;
import net.labyfy.component.inject.implement.Implement;

import java.util.function.Predicate;

@Singleton
@Implement(JsonConfigSerializer.class)
public class DefaultJsonConfigSerializer implements JsonConfigSerializer {

  private final Gson gson;

  @Inject
  public DefaultJsonConfigSerializer() {
    this.gson = new Gson();
  }

  @Override
  public void serialize(ParsedConfig config, JsonObject object, Predicate<ConfigObjectReference> predicate) {
    for (ConfigObjectReference reference : config.getConfigReferences()) {
      if (!predicate.test(reference)) {
        continue;
      }

      String[] keys = reference.getPathKeys();
      if (keys.length == 0) {
        continue;
      }

      JsonElement value = this.gson.toJsonTree(reference.getValue(config));
      JsonObject parent = object;

      for (int i = 0; i < keys.length - 1; i++) {
        String pathKey = keys[i];

        JsonObject nextParent = parent.getAsJsonObject(pathKey);

        if (nextParent == null) {
          nextParent = new JsonObject();
          parent.add(pathKey, nextParent);
        }

        parent = nextParent;
      }

      parent.add(keys[keys.length - 1], value);
    }
  }

  @Override
  public void deserialize(JsonObject object, ParsedConfig config, Predicate<ConfigObjectReference> predicate) {
    for (ConfigObjectReference reference : config.getConfigReferences()) {
      if (!predicate.test(reference)) {
        continue;
      }

      String[] keys = reference.getPathKeys();
      JsonObject parent = object;

      for (int i = 0; i < keys.length - 1; i++) {
        String pathKey = keys[i];

        if (parent == null) {
          break;
        }
        parent = parent.getAsJsonObject(pathKey);
      }

      if (parent == null) {
        continue;
      }

      JsonElement value = parent.get(keys[keys.length - 1]);
      if (value == null || value.isJsonNull()) {
        continue;
      }

      Object deserialized = this.gson.fromJson(value, reference.getSerializedType());
      reference.setValue(config, deserialized);
    }
  }
}
