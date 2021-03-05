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

package net.flintmc.mcapi.v1_16_5.player.type.hand;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.player.type.hand.HandMapper;
import net.minecraft.util.HandSide;

/**
 * 1.16.5 implementation of {@link HandMapper}.
 */
@Singleton
@Implement(HandMapper.class)
public class VersionedHandMapper implements HandMapper {

  /**
   * {@inheritDoc}
   */
  @Override
  public Hand fromMinecraftHand(Object hand) {
    if (!(hand instanceof net.minecraft.util.Hand)) {
      throw new IllegalArgumentException("");
    }

    net.minecraft.util.Hand minecraftHand = (net.minecraft.util.Hand) hand;

    return minecraftHand == net.minecraft.util.Hand.OFF_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftHand(Hand hand) {
    return hand == Hand.OFF_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Hand.Side fromMinecraftHandSide(Object handSide) {
    if (!(handSide instanceof HandSide)) {
      throw new IllegalArgumentException("");
    }

    HandSide minecraftHandSide = (HandSide) handSide;

    return minecraftHandSide == HandSide.RIGHT ? Hand.Side.RIGHT : Hand.Side.LEFT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftHandSide(Hand.Side handSide) {
    return handSide == Hand.Side.RIGHT ? Hand.Side.RIGHT : Hand.Side.LEFT;
  }
}
