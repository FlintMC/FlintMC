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

package net.flintmc.mcapi.v1_15_2.world.generator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.generator.WorldGameMode;
import net.flintmc.mcapi.world.generator.WorldGeneratorBuilder;
import net.flintmc.mcapi.world.generator.WorldGeneratorBuilder.Factory;
import net.flintmc.mcapi.world.generator.WorldGeneratorMapper;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import net.flintmc.mcapi.world.type.WorldTypeRegistry;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.FlatGenerationSettings;

@Singleton
@Implement(value = WorldGeneratorMapper.class, version = "1.15.2")
public class VersionedWorldGeneratorMapper implements WorldGeneratorMapper {

  private final WorldTypeRegistry typeRegistry;
  private final WorldGeneratorBuilder.Factory builderFactory;
  private final FlatWorldGeneratorSettings.Factory flatSettingsFactory;

  @Inject
  private VersionedWorldGeneratorMapper(
      WorldTypeRegistry typeRegistry,
      Factory builderFactory,
      FlatWorldGeneratorSettings.Factory flatSettingsFactory) {
    this.typeRegistry = typeRegistry;
    this.builderFactory = builderFactory;
    this.flatSettingsFactory = flatSettingsFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldGeneratorBuilder fromMinecraftGenerator(Object generator) {
    if (!(generator instanceof WorldSettings)) {
      throw new IllegalArgumentException(
          "Object needs to be an instance of " + WorldSettings.class.getName());
    }

    WorldSettings settings = (WorldSettings) generator;

    WorldGeneratorBuilder builder = this.builderFactory.newBuilder();

    builder.extended()
        .seed(settings.getSeed())
        .mode(settings.getHardcoreEnabled() ? WorldGameMode.HARDCORE
            : settings.getGameType() == GameType.CREATIVE ? WorldGameMode.CREATIVE
                : WorldGameMode.SURVIVAL)
        .type(this.typeRegistry.getType(settings.getTerrainType().getName()))
        .generateStructures(settings.isMapFeaturesEnabled())
        .allowCheats(settings.areCommandsAllowed())
        .bonusChest(settings.isBonusChestEnabled());

    // TODO read json

    return builder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftGenerator(WorldGeneratorBuilder generator) {
    generator.validate();

    GameType type;
    switch (generator.extended().mode()) {
      case SURVIVAL:
      case HARDCORE:
        type = GameType.SURVIVAL;
        break;
      case CREATIVE:
        type = GameType.CREATIVE;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + generator.extended().mode());
    }

    WorldType worldType = WorldType.byName(generator.extended().type().getName());
    if (worldType == null) {
      throw new IllegalArgumentException(
          String.format("WorldType %s doesn't exist", generator.extended().type().getName()));
    }

    WorldSettings settings = new WorldSettings(
        generator.extended().seed(),
        type,
        generator.extended().generateStructures(),
        generator.extended().mode() == WorldGameMode.HARDCORE,
        worldType
    );

    if (generator.extended().allowCheats()) {
      settings.enableCommands();
    }
    if (generator.extended().bonusChest()) {
      settings.enableBonusChest();
    }

    // TODO fill json

    return settings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings fromMinecraftFlatSettings(Object settings) {
    if (!(settings instanceof FlatGenerationSettings)) {
      throw new IllegalArgumentException(
          "Object needs to be an instance of " + FlatGenerationSettings.class.getName());
    }

    FlatGenerationSettings handle = (FlatGenerationSettings) settings;

    FlatWorldGeneratorSettings result = this.flatSettingsFactory.createEmpty();

    // TODO

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftFlatSettings(FlatWorldGeneratorSettings settings) {
    FlatGenerationSettings handle = new FlatGenerationSettings();

    // TODO fill

    return handle;
  }
}
