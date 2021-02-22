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

package net.flintmc.mcapi.settings.game;

import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.annotation.ExcludeStorage;
import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.mcapi.settings.flint.annotation.ui.InternalCategory;
import net.flintmc.mcapi.settings.game.configuration.AccessibilityConfiguration;
import net.flintmc.mcapi.settings.game.configuration.ChatConfiguration;
import net.flintmc.mcapi.settings.game.configuration.DebugConfiguration;
import net.flintmc.mcapi.settings.game.configuration.GraphicConfiguration;
import net.flintmc.mcapi.settings.game.configuration.KeyBindingConfiguration;
import net.flintmc.mcapi.settings.game.configuration.MouseConfiguration;
import net.flintmc.mcapi.settings.game.configuration.ResourcePackConfiguration;
import net.flintmc.mcapi.settings.game.configuration.SkinConfiguration;
import net.flintmc.mcapi.settings.game.configuration.SoundConfiguration;
import net.flintmc.mcapi.world.type.difficulty.Difficulty;

/**
 * Represents the Minecraft game settings
 */
@Config
@ImplementedConfig
@ExcludeStorage("local")
@InternalCategory("minecraft")
public interface MinecraftConfiguration {

  /**
   * Retrieves the accessibility configuration.
   *
   * @return The configuration of the accessibility.
   */
  AccessibilityConfiguration getAccessibilityConfiguration();

  /**
   * Retrieves the chat configuration.
   *
   * @return The configuration of the chat.
   */
  ChatConfiguration getChatConfiguration();

  /**
   * Retrieves the debug configuration.
   *
   * @return The configuration of debug.
   */
  DebugConfiguration getDebugConfiguration();

  /**
   * Retrieves the graphic configuration.
   *
   * @return The configuration of the graphics.
   */
  GraphicConfiguration getGraphicConfiguration();

  /**
   * Retrieves the key binding configuration.
   *
   * @return The configuration of key bindings.
   */
  KeyBindingConfiguration getKeyBindingConfiguration();

  /**
   * Retrieves the mouse configuration.
   *
   * @return The configuration of the mouse.
   */
  MouseConfiguration getMouseConfiguration();

  /**
   * Retrieves the resource pack configuration.
   *
   * @return The configuration of the resource pack.
   */
  ResourcePackConfiguration getResourcePackConfiguration();

  /**
   * Retrieves the skin configuration.
   *
   * @return The configuration of the skin.
   */
  SkinConfiguration getSkinConfiguration();

  /**
   * Retrieves the sound configuration.
   *
   * @return The configuration of sounds.
   */
  SoundConfiguration getSoundConfiguration();

  /**
   * Whether the RealMS notifications are displayed.
   *
   * @return {@code true} if the RealMS notifications are displayed, otherwise {@code false}.
   */
  boolean isRealmsNotifications();

  /**
   * Changes the state whether the RealMS notifications are displayed.
   *
   * @param realmsNotifications The new state.
   */
  void setRealmsNotifications(boolean realmsNotifications);

  /**
   * Retrieves the difficulty.
   *
   * @return The current difficulty.
   */
  Difficulty getDifficulty();

  /**
   * Changes the difficulty.
   *
   * @param difficulty The new difficulty.
   */
  void setDifficulty(Difficulty difficulty);

  /**
   * Saves and reload the Minecraft options.
   *
   * <p><b>Note:</b> This is needed to save configurations that have not been changed via the
   * Minecraft settings. The changes to the configuration will be loaded immediately so that the
   * client can use the changes.
   */
  void saveAndReloadOptions();
}
