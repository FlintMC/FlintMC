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

package net.flintmc.mcapi.render.image.util;

import net.flintmc.mcapi.player.network.NetworkPlayerInfo;

/**
 * Renderer for the head texture of players that are online on the current server.
 */
public interface PlayerHeadRenderer {

  /**
   * Draws the head of the given info at the given position with the given size.
   *
   * @param x    The x position of the top-left corner of the head to be rendered on the screen
   * @param y    The y position of the top-left corner of the head to be rendered on the screen
   * @param size The width and height in pixels of the head to be rendered on the screen
   * @param info The non-null info containing the resource location to the skin of the player
   */
  void drawPlayerHead(float x, float y, float size, NetworkPlayerInfo info);
}
