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

package net.flintmc.framework.config.serialization;

import com.google.gson.JsonElement;
import net.flintmc.framework.config.annotation.Config;

/**
 * Handler for the serialization of values in a {@link Config}. To register one, {@link
 * ConfigSerializer} may be used.
 *
 * @param <T> The type that can be serialized by this handler
 * @see ConfigSerializer
 */
public interface ConfigSerializationHandler<T> {

  /**
   * Serializes the given object into a {@link JsonElement}.
   *
   * @param t The non-null value to be serialized
   * @return The new non-null json element containing the serialized object
   */
  JsonElement serialize(T t);

  /**
   * Deserializes the given {@link JsonElement} into an object.
   *
   * @param element The non-null json element to be deserialized
   * @return The new non-null object from the given json element
   */
  T deserialize(JsonElement element);

}
