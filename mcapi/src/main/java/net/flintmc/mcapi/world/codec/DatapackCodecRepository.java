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

import java.util.Map;

/**
 * Represents a repository to register and unregister {@link DatapackCodec}.
 */
public interface DatapackCodecRepository {

  /**
   * Registers a new {@link DatapackCodec} with the given {@code name}.
   *
   * @param name          The name of the datapack codec.
   * @param datapackCodec The datapack codec to be registered.
   */
  void register(String name, DatapackCodec datapackCodec);

  /**
   * Unregisters a {@link DatapackCodec} with the given {@code name}.
   *
   * @param name The name of the datapack to be unregistered.
   */
  void unregister(String name);

  /**
   * Unregisters a {@link DatapackCodec} with the given {@code name} and the {@code datapackCodec}.
   *
   * @param name          The name of the datapack to be unregistered.
   * @param datapackCodec The datapack to be unregistered.
   */
  void unregister(String name, DatapackCodec datapackCodec);

  /**
   * Retrieves a registered {@link DatapackCodec} with the given {@code name}.
   *
   * @param name The name of a registered {@link DatapackCodec}.
   * @return A registered datapack codec with the given name or {@code null}.
   */
  DatapackCodec getDatapackCodec(String name);

  /**
   * Retrieves a map with all registered {@link DatapackCodec}.a
   *
   * @return A map with all registered datapack codecs.
   */
  Map<String, DatapackCodec> getDatapacks();
}
