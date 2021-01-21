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

package net.flintmc.mcapi.v1_16_5.player.type.model;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.type.model.ModelMapper;
import net.flintmc.mcapi.player.type.model.PlayerClothing;
import net.minecraft.entity.player.PlayerModelPart;

@Singleton
@Implement(value = ModelMapper.class, version = "1.16.5")
public class VersionedModelMapper implements ModelMapper {

  @Override
  public PlayerClothing fromMinecraftPlayerModelPart(Object playerModelPart) {
    if (!(playerModelPart instanceof PlayerModelPart)) {
      throw new IllegalArgumentException("");
    }

    PlayerModelPart modelPart = (PlayerModelPart) playerModelPart;

    switch (modelPart) {
      case CAPE:
        return PlayerClothing.CLOAK;
      case JACKET:
        return PlayerClothing.JACKET;
      case LEFT_SLEEVE:
        return PlayerClothing.LEFT_SLEEVE;
      case RIGHT_SLEEVE:
        return PlayerClothing.RIGHT_SLEEVE;
      case LEFT_PANTS_LEG:
        return PlayerClothing.LEFT_PANTS_LEG;
      case RIGHT_PANTS_LEG:
        return PlayerClothing.RIGHT_PANTS_LEG;
      case HAT:
        return PlayerClothing.HAT;
      default:
        throw new IllegalStateException("Unexpected value: " + modelPart);
    }
  }

  @Override
  public Object toMinecraftPlayerModelPart(PlayerClothing playerClothing) {
    switch (playerClothing) {
      case CLOAK:
        return PlayerModelPart.CAPE;
      case JACKET:
        return PlayerModelPart.JACKET;
      case LEFT_SLEEVE:
        return PlayerModelPart.LEFT_SLEEVE;
      case RIGHT_SLEEVE:
        return PlayerModelPart.RIGHT_SLEEVE;
      case LEFT_PANTS_LEG:
        return PlayerModelPart.LEFT_PANTS_LEG;
      case RIGHT_PANTS_LEG:
        return PlayerModelPart.RIGHT_PANTS_LEG;
      case HAT:
        return PlayerModelPart.HAT;
      default:
        throw new IllegalStateException("Unexpected value: " + playerClothing);
    }
  }
}
