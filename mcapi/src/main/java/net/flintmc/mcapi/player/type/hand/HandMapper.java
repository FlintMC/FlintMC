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

package net.flintmc.mcapi.player.type.hand;

/**
 * Mapper between the Minecraft hand, handSide and the Flint {@link Hand}, {@link Hand.Side}.
 */
public interface HandMapper {

  /**
   * Retrieves a {@link Hand} constant by using the given Minecraft hand.
   *
   * @param hand The non-null minecraft hand.
   * @return The {@link Hand} constant.
   * @throws IllegalArgumentException If the given object is not a Minecraft hand.
   */
  Hand fromMinecraftHand(Object hand);

  /**
   * Retrieves a Minecraft hand constant by using the given {@link Hand} .
   *
   * @param hand The non-null hand.
   * @return The hand constant.
   */
  Object toMinecraftHand(Hand hand);

  /**
   * Retrieves a {@link Hand.Side} constant by using the given Minecraft hand side.
   *
   * @param handSide The non-null minecraft hand side.
   * @return The {@link Hand.Side} constant.
   * @throws IllegalArgumentException If the given object is not a Minecraft hand side.
   */
  Hand.Side fromMinecraftHandSide(Object handSide);

  /**
   * Retrieves a Minecraft hand side constant by using the given {@link Hand.Side} .
   *
   * @param handSide The non-null minecraft hand side.
   * @return The hand side constant.
   */
  Object toMinecraftHandSide(Hand.Side handSide);
}
