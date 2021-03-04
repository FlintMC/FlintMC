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

package net.flintmc.mcapi.v1_15_2.settings.game.configuration;

import com.google.inject.Singleton;
import java.util.Set;
import java.util.stream.Collectors;
import net.flintmc.framework.config.annotation.implemented.ConfigImplementation;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.player.type.model.PlayerClothing;
import net.flintmc.mcapi.settings.game.configuration.SkinConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.HandSide;

/**
 * 1.15.2 implementation of {@link SkinConfiguration}.
 */
@Singleton
@ConfigImplementation(value = SkinConfiguration.class)
public class VersionedSkinConfiguration implements SkinConfiguration {

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<PlayerClothing> getPlayerClothing() {
    return Minecraft.getInstance().gameSettings.getModelParts().stream()
        .map(this::fromMinecraftObject)
        .collect(Collectors.toSet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setModelClothingEnabled(PlayerClothing clothing, boolean enable) {
    Minecraft.getInstance()
        .gameSettings
        .setModelPartEnabled(this.toMinecraftObject(clothing), enable);
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isModelClothingEnabled(PlayerClothing clothing) {
    PlayerModelPart targetPart = this.toMinecraftObject(clothing);
    for (PlayerModelPart part : Minecraft.getInstance().gameSettings.getModelParts()) {
      if (part == targetPart) {
        return true;
      }
    }

    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void switchModelClothingEnabled(PlayerClothing clothing) {
    Minecraft.getInstance().gameSettings.switchModelPartEnabled(this.toMinecraftObject(clothing));
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Hand.Side getMainHand() {
    switch (Minecraft.getInstance().gameSettings.mainHand) {
      case LEFT:
        return Hand.Side.LEFT;
      case RIGHT:
        return Hand.Side.RIGHT;
      default:
        throw new IllegalStateException(
            "Unexpected value: " + Minecraft.getInstance().gameSettings.mainHand);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMainHand(Hand.Side mainHand) {
    switch (mainHand) {
      case LEFT:
        Minecraft.getInstance().gameSettings.mainHand = HandSide.LEFT;
        break;
      case RIGHT:
        Minecraft.getInstance().gameSettings.mainHand = HandSide.RIGHT;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + mainHand);
    }
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  private PlayerClothing fromMinecraftObject(PlayerModelPart playerModelPart) {
    switch (playerModelPart) {
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
        throw new IllegalStateException("Unexpected value: " + playerModelPart);
    }
  }

  private PlayerModelPart toMinecraftObject(PlayerClothing clothing) {
    switch (clothing) {
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
        throw new IllegalStateException("Unexpected value: " + clothing);
    }
  }
}
