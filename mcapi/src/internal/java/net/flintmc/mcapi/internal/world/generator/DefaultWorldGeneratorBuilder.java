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
import net.flintmc.mcapi.world.generator.WorldGenerator;
import net.flintmc.mcapi.world.generator.WorldGeneratorBuilder;
import net.flintmc.mcapi.world.generator.buffet.BuffetWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;

@Implement(WorldGeneratorBuilder.class)
public class DefaultWorldGeneratorBuilder implements WorldGeneratorBuilder {

  private final WorldGenerator generator;
  private final ExtendedWorldGeneratorSettings.Factory extendedFactory;
  private final FlatWorldGeneratorSettings.Factory flatFactory;
  private final BuffetWorldGeneratorSettings.Factory buffetFactory;

  private String name;
  private ExtendedWorldGeneratorSettings extended;
  private FlatWorldGeneratorSettings flatSettings;
  private BuffetWorldGeneratorSettings buffetSettings;

  @AssistedInject
  public DefaultWorldGeneratorBuilder(
      WorldGenerator generator,
      ExtendedWorldGeneratorSettings.Factory extendedFactory,
      FlatWorldGeneratorSettings.Factory flatFactory,
      BuffetWorldGeneratorSettings.Factory buffetFactory) {
    this.generator = generator;
    this.extendedFactory = extendedFactory;
    this.flatFactory = flatFactory;
    this.buffetFactory = buffetFactory;
  }

  @Override
  public WorldGeneratorBuilder name(String name) {
    this.name = name;
    return this;
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public String findFileName() {
    return null; // TODO
  }

  @Override
  public ExtendedWorldGeneratorSettings extended() {
    if (this.extended == null) {
      this.extended = this.extendedFactory.create();
    }

    return this.extended;
  }

  @Override
  public WorldGeneratorBuilder extended(ExtendedWorldGeneratorSettings extended) {
    this.extended = extended;
    return this;
  }

  @Override
  public FlatWorldGeneratorSettings flatSettings() {
    if (this.flatSettings == null) {
      this.flatSettings = this.flatFactory.createDefault();
    }

    return this.flatSettings;
  }

  @Override
  public WorldGeneratorBuilder flatSettings(FlatWorldGeneratorSettings settings) {
    this.flatSettings = settings;
    return this;
  }

  @Override
  public BuffetWorldGeneratorSettings buffetSettings() {
    if (this.buffetSettings == null) {
      this.buffetSettings = this.buffetFactory.createDefault();
    }

    return this.buffetSettings;
  }

  @Override
  public WorldGeneratorBuilder buffetSettings(BuffetWorldGeneratorSettings settings) {
    this.buffetSettings = settings;
    return this;
  }

  @Override
  public WorldGeneratorBuilder validate() {
    Preconditions.checkNotNull(this.name, "No name set");
    this.extended().validate();

    if (this.flatSettings != null) {
      this.flatSettings.validate();
    }
    if (this.buffetSettings != null) {
      this.buffetSettings.validate();
    }

    return this;
  }

  @Override
  public void generateAndJoin() {
    this.generator.generateAndJoin(this.validate());
  }
}
