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

package net.flintmc.mcapi.world.stats.value;

import net.flintmc.framework.generation.annotation.DataFactory;
import net.flintmc.framework.generation.annotation.TargetDataField;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.world.stats.StatsCategory;
import net.flintmc.mcapi.world.stats.WorldStats;

/**
 * Represents a world stat for the number of kills by and towards a specific entity type from the
 * {@link WorldStats}.
 */
public interface MobWorldStat extends WorldStat {

  /**
   * Retrieves the entity type that is represented by this statistic.
   *
   * @return The non-null entity type of this statistic
   */
  @TargetDataField("entityType")
  EntityType getEntityType();

  /**
   * Retrieves the component that represents the number of kills for this entity type.
   *
   * @return The non-null component with the number of kills
   */
  @TargetDataField("killedComponent")
  ChatComponent getKilledComponent();

  /**
   * Retrieves the component that represents the number of kills by this entity type.
   *
   * @return The non-null component with the number of kills
   */
  @TargetDataField("killedByComponent")
  ChatComponent getKilledByComponent();

  /**
   * Retrieves the number of entities that have been killed by the player of this entity type.
   *
   * @return The number of kills
   */
  @TargetDataField("killedCount")
  int getKilledCount();

  /**
   * Retrieves the number of times the player has been killed by an entity of this type.
   *
   * @return The number of kills
   */
  @TargetDataField("killedByCount")
  int getKilledByCount();

  /**
   * Factory for the {@link MobWorldStat}.
   */
  @DataFactory(MobWorldStat.class)
  interface Factory {

    /**
     * Creates a new {@link MobWorldStat}.
     *
     * @param category          The non-null category of the new statistic
     * @param entityType        The non-null entity type of the new statistic
     * @param killedComponent   The non-null component that represents the number of kills
     * @param killedByComponent The non-null component that represents the number of kills by the
     *                          entities of the given entity type
     * @param killedCount       The number of entities that have been killed by the player of the
     *                          given entity type
     * @param killedByCount     The number of times the player has been killed by an entity of the
     *                          given entity type
     * @return THe new non-null {@link MobWorldStat}
     */
    MobWorldStat create(
        @TargetDataField("category") StatsCategory category,
        @TargetDataField("entityType") EntityType entityType,
        @TargetDataField("killedComponent") ChatComponent killedComponent,
        @TargetDataField("killedByComponent") ChatComponent killedByComponent,
        @TargetDataField("killedCount") int killedCount,
        @TargetDataField("killedByCount") int killedByCount);
  }
}
