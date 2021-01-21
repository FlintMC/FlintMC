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

package net.flintmc.mcapi.v1_16_5.world.biome;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.mcapi.world.biome.Biome;
import net.flintmc.mcapi.world.biome.BiomeRegistry;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;

@Singleton
@Implement(value = BiomeRegistry.class, version = "1.16.5")
public class VersionedBiomeRegistry implements BiomeRegistry {

  private final Map<ResourceLocation, Biome> biomes;
  private final Biome defaultBiome;

  @Inject
  private VersionedBiomeRegistry(ResourceLocationProvider provider, Biome.Factory biomeFactory) {
    this.biomes = new HashMap<>();

    DynamicRegistries registries = DynamicRegistries.func_239770_b_();
    Registry<net.minecraft.world.biome.Biome> registry = registries.getRegistry(Registry.BIOME_KEY);
    for (net.minecraft.world.biome.Biome biome : registry) {
      ResourceLocation location = provider.fromMinecraft(registry.getKey(biome));
      Biome flintBiome = biomeFactory.create(location, biome);
      this.biomes.put(flintBiome.getName(), flintBiome);
    }

    net.minecraft.world.biome.Biome defaultBiome = registry.getOrThrow(Biomes.PLAINS);
    this.defaultBiome = this.getBiome(provider.fromMinecraft(registry.getKey(defaultBiome)));
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
