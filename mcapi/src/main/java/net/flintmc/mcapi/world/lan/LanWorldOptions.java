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

package net.flintmc.mcapi.world.lan;

import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.player.type.GameMode;

/**
 * Options for opening a world to the LAN via the {@link LanWorldService}.
 */
public interface LanWorldOptions {

  /**
   * Changes the game mode that should be used for the LAN world, defaults to {@link
   * GameMode#SURVIVAL}.
   *
   * @param mode The non-null game mode
   * @return this instance for chaining
   */
  LanWorldOptions mode(GameMode mode);

  /**
   * Retrieves the game mode that should be used for the LAN world, defaults to {@link
   * GameMode#SURVIVAL}.
   *
   * @return The non-null game mode
   */
  GameMode mode();

  /**
   * Changes whether cheats should be allowed on the LAN world, defaults to {@code false}.
   *
   * @param allowCheats {@code true} if cheats should be allowed, {@code false} otherwise
   * @return this instance for chaining
   */
  LanWorldOptions allowCheats(boolean allowCheats);

  /**
   * Retrieves whether cheats should be allowed on the LAN world, defaults to {@code false}.
   *
   * @return {@code true} if cheats should be allowed, {@code false} otherwise
   */
  boolean allowCheats();

  /**
   * Changes whether the player should receive a message that the LAN world has either been
   * successfully opened or failed to open, defaults to {@code true}.
   *
   * @param showInfoMessage {@code true} if the message should be sent, {@code false} otherwise
   * @return this instance for chaining
   */
  LanWorldOptions showInfoMessage(boolean showInfoMessage);

  /**
   * Retrieves whether the player should receive a message that the LAN world has either been
   * successfully opened or failed to open, defaults to {@code true}.
   *
   * @return {@code true} if the message should be sent, {@code false} otherwise
   */
  boolean showInfoMessage();

  /**
   * Factory for the {@link LanWorldOptions}.
   */
  @AssistedFactory(LanWorldOptions.class)
  interface Factory {

    /**
     * Creates a new empty {@link LanWorldOptions}.
     *
     * @return The new non-null {@link LanWorldOptions} instance
     */
    LanWorldOptions create();

  }

}
