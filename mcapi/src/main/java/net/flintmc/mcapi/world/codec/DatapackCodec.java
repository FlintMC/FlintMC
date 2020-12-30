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

package net.flintmc.mcapi.world.codec;

import java.util.List;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/** Represents a datapack codec. */
public interface DatapackCodec {

  /**
   * Retrieves a collection with all enabled datapack codecs.
   *
   * @return A collection with all enabled datapack codecs.
   */
  List<String> getEnabled();

  /**
   * Retrieves a collection with all disabled datapack codecs.
   *
   * @return A collection with all disabled datapack codecs.
   */
  List<String> getDisabled();

  /** Factory for {@link DatapackCodec}. */
  @AssistedFactory(DatapackCodec.class)
  interface Factory {

    /**
     * Creates a new {@link DatapackCodec} with the given parameters.
     *
     * @param enabled A collection of all enabled datapack codecs.
     * @param disabled A collection of all disabled datapack codecs.
     * @return A created datapack codec.
     */
    DatapackCodec create(List<String> enabled, List<String> disabled);
  }
}
