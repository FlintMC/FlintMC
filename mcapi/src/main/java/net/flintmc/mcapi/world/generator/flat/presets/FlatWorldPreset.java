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

package net.flintmc.mcapi.world.generator.flat.presets;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;

/**
 * Represents a preset for {@link FlatWorldGeneratorSettings}.
 */
public interface FlatWorldPreset {

  /**
   * Retrieves the display name of this preset as it will be displayed.
   *
   * @return The non-null display name
   */
  ChatComponent getDisplayName();

  /**
   * Retrieves the item that should be used as the icon of this preset.
   *
   * @return The non-null icon
   */
  ItemStack getIcon();

  /**
   * Retrieves the settings of this preset for the flat world generator.
   *
   * @return The non-null flat generator settings
   */
  FlatWorldGeneratorSettings getSettings();

  /**
   * Factory for the {@link FlatWorldPreset}.
   */
  @AssistedFactory(FlatWorldPreset.class)
  interface Factory {

    /**
     * Creates a new {@link FlatWorldPreset}.
     *
     * @param displayName The non-null display name
     * @param icon        The non-null item that should be used as the icon of this preset
     * @param settings    The non-null flat generator settings of this preset
     * @return The new non-null {@link FlatWorldPreset}
     */
    FlatWorldPreset create(
        @Assisted ChatComponent displayName,
        @Assisted ItemStack icon,
        @Assisted FlatWorldGeneratorSettings settings);

  }

}
