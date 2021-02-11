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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.serialization.ConfigSerializationHandler;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.config.serialization.ConfigSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

@Singleton
@ConfigSerializer(Collection.class)
public class CollectionConfigSerializer implements ConfigSerializationHandler<Collection<?>> {

  private final ConfigSerializationService serializationService;

  @Inject
  private CollectionConfigSerializer(ConfigSerializationService serializationService) {
    this.serializationService = serializationService;
  }

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
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
