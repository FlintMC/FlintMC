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

package net.flintmc.mcapi.v1_15_2.world.biome;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.eventbus.event.subscribe.PreSubscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.registry.RegistryRegisterEvent;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.mcapi.world.biome.Biome;
import net.flintmc.mcapi.world.biome.BiomeRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;

@Singleton
@Implement(BiomeRegistry.class)
public class VersionedBiomeRegistry implements BiomeRegistry {

  private final Map<ResourceLocation, Biome> biomes;
  private final Biome.Factory biomeFactory;
  private final ResourceLocationProvider provider;
  private Biome defaultBiome;

  @Inject
  private VersionedBiomeRegistry(ResourceLocationProvider provider, Biome.Factory biomeFactory) {
    this.biomes = new HashMap<>();
    this.biomeFactory = biomeFactory;
    this.provider = provider;
  }

  @PreSubscribe
  public void registerBiomes(final RegistryRegisterEvent event) {
    if (!event.getRegistryKeyLocation().getPath().equals("biome")) {
      return;
    }

    Biome biome = this.biomeFactory.create(event.getRegistryValueLocation(),
        event.getRegistryObject());
    this.biomes.put(biome.getName(), biome);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<Biome> getBiomes() {
    return this.biomes.values();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Biome getDefaultBiome() {
    if (this.defaultBiome == null) {
      this.defaultBiome = this.getBiome(
          this.provider.fromMinecraft(Registry.BIOME.getKey(Biomes.PLAINS)));
    }

    return this.defaultBiome;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Biome getBiome(ResourceLocation name) {
    return this.biomes.get(name);
  }
}
