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

package net.flintmc.mcapi.nbt.serializer;

import com.google.gson.JsonElement;
import net.flintmc.mcapi.nbt.NBT;

/**
 * A serializer which serializes a {@link NBT} to a {@link JsonElement} or deserializes a {@link
 * JsonElement} to a {@link NBT}.
 */
public interface NBTSerializer {

  /**
   * Serializes the given {@link NBT} to a {@link JsonElement}.
   *
   * @param nbt The named binary tag to serialize.
   * @return The serialized nbt as a {@link JsonElement}.
   * @throws AssertionError                If thrown when a terminated named binary tag comes,
   *                                       should not be encountered.
   * @throws UnsupportedOperationException If thrown when a new NBT class is made.
   */
  JsonElement serialize(NBT nbt);

  /**
   * Deserializes the given {@link JsonElement} to a {@link NBT}.
   *
   * @param element The json to deserialize.
   * @return The deserialized json as a {@link NBT}.
   * @throws AssertionError Is thrown when something goes wrong.
   */
  NBT deserialize(JsonElement element);
}
