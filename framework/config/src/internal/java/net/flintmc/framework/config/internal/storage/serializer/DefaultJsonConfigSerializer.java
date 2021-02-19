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

package net.flintmc.framework.config.internal.storage.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.function.Predicate;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.serialization.ConfigSerializationHandler;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.config.storage.serializer.JsonConfigSerializer;
import net.flintmc.framework.inject.implement.Implement;

@Singleton
@Implement(JsonConfigSerializer.class)
public class DefaultJsonConfigSerializer implements JsonConfigSerializer {

  private static final Predicate<ConfigObjectReference> TRUE = o -> true;

  private final Gson gson;
  private final ConfigSerializationService serializationService;

  @Inject
  private DefaultJsonConfigSerializer(ConfigSerializationService serializationService) {
    this.serializationService = serializationService;
    this.gson = new Gson();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonObject serialize(ParsedConfig config) {
    JsonObject object = new JsonObject();
    this.serialize(config, object, TRUE);
    return object;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deserialize(JsonObject object, ParsedConfig config) {
    this.deserialize(object, config, TRUE);
  }

  private ConfigSerializationHandler getHandler(ConfigObjectReference reference) {
    if (reference.getSerializedType() instanceof Class) {
      return this.serializationService.getSerializer((Class<?>) reference.getSerializedType());
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void serialize(
      ParsedConfig config, JsonObject object, Predicate<ConfigObjectReference> predicate) {
    for (ConfigObjectReference reference : config.getConfigReferences()) {
      if (!predicate.test(reference)) {
        continue;
      }

      String[] keys = reference.getPathKeys();
      if (keys.length == 0) {
        continue;
      }

      Object rawValue = reference.getValue();
      JsonElement value = null;

      ConfigSerializationHandler handler = this.getHandler(reference);
      if (handler != null && rawValue != null) {
        value = handler.serialize(rawValue);
      }

      if (value == null) {
        value = this.gson.toJsonTree(rawValue);
      }
      if (value.isJsonNull()) {
        continue;
      }

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

  /**
   * {@inheritDoc}
   */
  @Override
  public void deserialize(
      JsonObject object, ParsedConfig config, Predicate<ConfigObjectReference> predicate) {
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
        Object defaultValue = reference.getDefaultValue();
        if (defaultValue != null) {
          reference.setValue(defaultValue);
        }
        continue;
      }

      Object deserialized = null;
      ConfigSerializationHandler handler = this.getHandler(reference);
      if (handler != null) {
        deserialized = handler.deserialize(value);
      }

      if (deserialized == null) {
        deserialized = this.gson.fromJson(value, reference.getSerializedType());
      }

      if (deserialized != null) {
        reference.setValue(deserialized);
      }
    }
  }
}
