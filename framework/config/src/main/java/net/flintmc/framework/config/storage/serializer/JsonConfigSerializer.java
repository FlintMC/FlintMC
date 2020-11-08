package net.flintmc.framework.config.storage.serializer;

import com.google.gson.JsonObject;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.generator.ParsedConfig;

import java.util.function.Predicate;

/**
 * Serializer for {@link ConfigObjectReference}s into a json object.
 */
public interface JsonConfigSerializer {

  /**
   * Serializes the references inside of the given config into the given {@link JsonObject}, overrides any values that
   * are already in that {@link JsonObject}. The json can be deserialized into a config again with {@link
   * #deserialize(JsonObject, ParsedConfig, Predicate)}.
   *
   * @param config    The non-null config with the references to be serialized
   * @param object    The non-null target json object
   * @param predicate The non-null predicate to test whether a reference should be serialized or not
   */
  void serialize(ParsedConfig config, JsonObject object, Predicate<ConfigObjectReference> predicate);

  /**
   * Deserializes the given {@link JsonObject} into the references inside of the given config, if a value is not
   * specified in the given {@link JsonObject}, it won't be changed. The config can be serialized again into a json
   * object with {@link #serialize(ParsedConfig, JsonObject, Predicate)}.
   *
   * @param config    The non-null target config with the references
   * @param predicate The non-null predicate to test whether a reference should be serialized or not
   * @param object    The non-null json object to be serialized
   */
  void deserialize(JsonObject object, ParsedConfig config, Predicate<ConfigObjectReference> predicate);

}
