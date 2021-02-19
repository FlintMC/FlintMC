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

package net.flintmc.framework.config.internal.serialization.defaults;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.config.serialization.ConfigSerializationHandler;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.config.serialization.ConfigSerializer;

@Singleton
@ConfigSerializer(Map.class)
public class MapConfigSerializer implements ConfigSerializationHandler<Map<?, ?>> {

  private final ConfigSerializationService serializationService;

  @Inject
  private MapConfigSerializer(ConfigSerializationService serializationService) {
    this.serializationService = serializationService;
  }

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
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
