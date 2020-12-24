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

package net.flintmc.mcapi.player.serializer;

/**
 * A serializer which serialized or deserialized objects.
 *
 * @param <S> The type to serialize or deserialize
 * @param <D> The type to serialize or deserialize
 */
public interface Serializer<S, D> {

  /**
   * Deserializes the given {@link S} type to the {@link D} type
   *
   * @param value The {@link S} being deserialized
   * @return A deserialized {@link D}
   */
  D deserialize(S value);

  /**
   * Serializes the given {@link D} type to the {@link S} type
   *
   * @param value The {@link D} being serialized
   * @return A serialized {@link S}
   */
  S serialize(D value);
}
