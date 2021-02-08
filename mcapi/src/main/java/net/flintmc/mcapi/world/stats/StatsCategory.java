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

package net.flintmc.mcapi.world.stats;

import net.flintmc.framework.generation.annotation.DataFactory;
import net.flintmc.framework.generation.annotation.TargetDataField;
import net.flintmc.mcapi.chat.component.ChatComponent;

/**
 * Category of stats that contains more information about a {@link WorldStatType}.
 */
public interface StatsCategory {

  /**
   * Retrieves the {@link WorldStatType} that identifies this category.
   *
   * @return The non-null world stat type of this category
   */
  @TargetDataField("type")
  WorldStatType getType();

  /**
   * Retrieves the display name of this category as it would be displayed in the menu.
   *
   * @return The non-null display name of this category
   */
  @TargetDataField("displayName")
  ChatComponent getDisplayName();

  /**
   * Factory for the {@link StatsCategory}.
   */
  @DataFactory(StatsCategory.class)
  interface Factory {

    /**
     * Creates a new {@link StatsCategory} for the given type and display name.
     *
     * @param type        The non-null type that identifies this category
     * @param displayName The non-null display name of the new category
     * @return The new non-null {@link StatsCategory}
     */
    StatsCategory create(
        @TargetDataField("type") WorldStatType type,
        @TargetDataField("displayName") ChatComponent displayName);
  }

}
