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

package net.flintmc.mcapi.settings.game.configuration;

import java.util.Set;
import net.flintmc.framework.config.annotation.ConfigExclude;
import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.player.type.model.PlayerClothing;
import net.flintmc.mcapi.settings.flint.annotation.ui.DefineCategory;
import net.flintmc.mcapi.settings.flint.options.selection.enumeration.EnumSelectSetting;

/**
 * Represents the skin configuration.
 */
@DefineCategory(
    name = "minecraft.settings.skin",
    displayName = @Component(value = "options.skinCustomisation", translate = true))
@ImplementedConfig
public interface SkinConfiguration {

  /**
   * Retrieves the main hand side.
   *
   * @return The main hand side.
   */
  @EnumSelectSetting
  Hand.Side getMainHand();

  /**
   * Changes the main hand side.
   *
   * @param mainHand The new main hand side.
   */
  void setMainHand(Hand.Side mainHand);

  /**
   * Retrieves a collection with all enabled player clothing.
   *
   * @return A collection with all enabled player clothing.
   */
  @ConfigExclude
  Set<PlayerClothing> getPlayerClothing();

  /**
   * Changes the state of the player clothing.
   *
   * @param clothing The player clothing to enable or disable.
   * @param state    {@code true} if the clothing should be enabled, otherwise {@code false}.
   */
  void setModelClothingEnabled(PlayerClothing clothing, boolean state);

  /**
   * Retrieves whether a clothing is enabled.
   *
   * @param clothing The non-null clothing to check for
   * @return {@code true} if the clothing is enabled, {@code false} otherwise
   */
  boolean isModelClothingEnabled(PlayerClothing clothing);

  /**
   * Switches the state of the given player clothing.
   *
   * @param clothing The player clothing to switch the state.
   */
  void switchModelClothingEnabled(PlayerClothing clothing);
}
