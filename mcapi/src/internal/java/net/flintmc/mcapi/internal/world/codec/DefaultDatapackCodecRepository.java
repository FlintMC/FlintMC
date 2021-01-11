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

package net.flintmc.mcapi.internal.world.codec;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.datapack.DatapackCodec;
import net.flintmc.mcapi.world.datapack.DatapackCodec.Factory;
import net.flintmc.mcapi.world.datapack.DatapackCodecRepository;

@Singleton
@Implement(DatapackCodecRepository.class)
public class DefaultDatapackCodecRepository implements DatapackCodecRepository {

  private final Map<String, DatapackCodec> datapackCodecs;

  @Inject
  private DefaultDatapackCodecRepository(Factory datapackCodecFactory) {
    this.datapackCodecs = Maps.newConcurrentMap();
    this.register(
        "vanilla", datapackCodecFactory.create(ImmutableList.of("vanilla"), ImmutableList.of()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void register(String name, DatapackCodec datapackCodec) {
    this.datapackCodecs.put(name, datapackCodec);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregister(String name) {
    this.datapackCodecs.remove(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregister(String name, DatapackCodec datapackCodec) {
    this.datapackCodecs.remove(name, datapackCodec);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DatapackCodec getDatapackCodec(String name) {
    return this.datapackCodecs.get(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, DatapackCodec> getDatapacks() {
    return this.datapackCodecs;
  }
}
