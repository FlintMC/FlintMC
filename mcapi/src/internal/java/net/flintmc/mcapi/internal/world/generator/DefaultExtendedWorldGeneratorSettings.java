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

package net.flintmc.mcapi.internal.world.generator;

import com.google.common.base.Preconditions;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.generator.ExtendedWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.WorldGameMode;
import net.flintmc.mcapi.world.type.WorldType;
import net.flintmc.mcapi.world.type.WorldTypeRegistry;

import java.util.Random;

@Implement(ExtendedWorldGeneratorSettings.class)
public class DefaultExtendedWorldGeneratorSettings implements ExtendedWorldGeneratorSettings {

  private final Random random;

  private boolean randomSeed = true;
  private long seed;
  private WorldGameMode mode = WorldGameMode.SURVIVAL;
  private WorldType type;
  private boolean generateStructures = true;
  private boolean allowCheats;
  private boolean bonusChest;

  @AssistedInject
  public DefaultExtendedWorldGeneratorSettings(WorldTypeRegistry typeRegistry) {
    this.random = new Random();
    this.type = typeRegistry.getDefaultType();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtendedWorldGeneratorSettings seed(String seed) {
    if (seed.isEmpty()) {
      this.randomSeed = true;
      return this;
    }

    try {
      return this.seed(Long.parseLong(seed));
    } catch (NumberFormatException ignored) {
      return this.seed(seed.hashCode());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtendedWorldGeneratorSettings seed(long seed) {
    this.randomSeed = false;
    this.seed = seed;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long seed() {
    if (this.randomSeed) {
      this.randomSeed = false;
      this.seed = this.random.nextLong();
    }

    return this.seed;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtendedWorldGeneratorSettings mode(WorldGameMode mode) {
    this.mode = mode;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldGameMode mode() {
    return this.mode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtendedWorldGeneratorSettings type(WorldType type) {
    this.type = type;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType type() {
    return this.type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtendedWorldGeneratorSettings generateStructures(boolean generateStructures) {
    this.generateStructures = generateStructures;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean generateStructures() {
    return this.generateStructures;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtendedWorldGeneratorSettings allowCheats(boolean allowCheats) {
    this.allowCheats = allowCheats;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean allowCheats() {
    return this.allowCheats;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtendedWorldGeneratorSettings bonusChest(boolean bonusChest) {
    this.bonusChest = bonusChest;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean bonusChest() {
    return this.bonusChest;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtendedWorldGeneratorSettings validate() {
    Preconditions.checkNotNull(this.mode, "Invalid mode set");
    Preconditions.checkNotNull(this.type, "Invalid type set");

    return this;
  }
}
