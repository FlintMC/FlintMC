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

package net.flintmc.mcapi.internal.world.generator.flat;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.presets.FlatWorldPreset;

@Implement(FlatWorldPreset.class)
public class DefaultFlatWorldPreset implements FlatWorldPreset {

  private final ChatComponent displayName;
  private final ItemStack icon;
  private final FlatWorldGeneratorSettings settings;

  @AssistedInject
  public DefaultFlatWorldPreset(
      @Assisted ChatComponent displayName,
      @Assisted ItemStack icon,
      @Assisted FlatWorldGeneratorSettings settings) {
    this.displayName = displayName;
    this.icon = icon;
    this.settings = settings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getIcon() {
    return this.icon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FlatWorldGeneratorSettings getSettings() {
    return this.settings;
  }
}
