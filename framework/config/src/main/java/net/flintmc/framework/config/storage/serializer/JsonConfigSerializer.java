/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.framework.config.storage.serializer;

import com.google.gson.JsonObject;
import java.util.function.Predicate;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;

/**
 * Serializer for {@link ConfigObjectReference}s into a json object.
 */
public interface JsonConfigSerializer {

  /**
   * Serializes the references inside of the given config into a new {@link JsonObject}. The json
   * can be deserialized into a config again with {@link #deserialize(JsonObject, ParsedConfig,
   * Predicate)}.
   *
   * @param config The non-null config with the references to be serialized
   */
  JsonObject serialize(ParsedConfig config);

  /**
   * Deserializes the given {@link JsonObject} into the references inside of the given config, if a
   * value is not specified in the given {@link JsonObject}, it won't be changed. The config can be
   * serialized again into a json object with {@link #serialize(ParsedConfig, JsonObject,
   * Predicate)}.
   *
   * @param config The non-null target config with the references
   * @param object The non-null json object to be serialized
   */
  void deserialize(JsonObject object, ParsedConfig config);

  /**
   * Serializes the references inside of the given config into the given {@link JsonObject},
   * overrides any values that are already in that {@link JsonObject}. The json can be deserialized
   * into a config again with {@link #deserialize(JsonObject, ParsedConfig, Predicate)}.
   *
   * @param config    The non-null config with the references to be serialized
   * @param object    The non-null target json object
   * @param predicate The non-null predicate to test whether a reference should be serialized or
   *                  not
   */
  void serialize(
      ParsedConfig config, JsonObject object, Predicate<ConfigObjectReference> predicate);

  /**
   * Deserializes the given {@link JsonObject} into the references inside of the given config, if a
   * value is not specified in the given {@link JsonObject}, it won't be changed. The config can be
   * serialized again into a json object with {@link #serialize(ParsedConfig, JsonObject,
   * Predicate)}.
   *
   * @param config    The non-null target config with the references
   * @param predicate The non-null predicate to test whether a reference should be serialized or
   *                  not
   * @param object    The non-null json object to be serialized
   */
  void deserialize(
      JsonObject object, ParsedConfig config, Predicate<ConfigObjectReference> predicate);
}
