package net.labyfy.component.config.storage.serializer;

import com.google.gson.JsonObject;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;

import java.util.function.Predicate;

public interface JsonConfigSerializer {

  void serialize(ParsedConfig config, JsonObject object, Predicate<ConfigObjectReference> predicate);

  void deserialize(JsonObject object, ParsedConfig config, Predicate<ConfigObjectReference> predicate);

}
