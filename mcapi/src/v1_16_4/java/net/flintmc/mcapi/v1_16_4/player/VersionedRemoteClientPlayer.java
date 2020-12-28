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

package net.flintmc.mcapi.v1_16_4.player;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.item.ItemEntityMapper;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.player.RemoteClientPlayer;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;
import net.flintmc.mcapi.player.network.NetworkPlayerInfoRegistry;
import net.flintmc.mcapi.player.serializer.gameprofile.GameProfileSerializer;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.player.type.model.ModelMapper;
import net.flintmc.mcapi.player.type.model.SkinModel;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.tileentity.mapper.TileEntityMapper;
import net.flintmc.mcapi.world.World;

/**
 * 1.16.4 implementation of the {@link RemoteClientPlayer}.
 */
@Implement(value = RemoteClientPlayer.class, version = "1.16.4")
public class VersionedRemoteClientPlayer extends VersionedPlayerEntity
        implements RemoteClientPlayer {

  private final net.minecraft.client.entity.player.RemoteClientPlayerEntity playerEntity;
  private final NetworkPlayerInfoRegistry networkPlayerInfoRegistry;

  @AssistedInject
  private VersionedRemoteClientPlayer(
          @Assisted("entity") Object entity,
          @Assisted("entityType") EntityType entityType,
          World world,
          EntityFoundationMapper entityFoundationMapper,
          GameProfileSerializer gameProfileSerializer,
          ModelMapper modelMapper,
          NetworkPlayerInfoRegistry networkPlayerInfoRegistry,
          ItemEntityMapper itemEntityMapper,
          TileEntityMapper tileEntityMapper) {
    super(
            entity,
            entityType,
            world,
            entityFoundationMapper,
            gameProfileSerializer,
            modelMapper,
            itemEntityMapper,
            tileEntityMapper);

    if (!(entity instanceof net.minecraft.client.entity.player.RemoteClientPlayerEntity)) {
      throw new IllegalArgumentException(
              entity.getClass().getName()
                      + " is not an instance of "
                      + net.minecraft.client.entity.player.RemoteClientPlayerEntity.class.getName());
    }
    this.playerEntity = (net.minecraft.client.entity.player.RemoteClientPlayerEntity) entity;
    this.networkPlayerInfoRegistry = networkPlayerInfoRegistry;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasPlayerInfo() {
    return this.getNetworkPlayerInfo() != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NetworkPlayerInfo getNetworkPlayerInfo() {
    return this.networkPlayerInfoRegistry.getPlayerInfo(playerEntity.getGameProfile().getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getFovModifier() {
    return this.playerEntity.getFovModifier();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SkinModel getSkinModel() {
    return this.getNetworkPlayerInfo().getSkinModel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getSkinLocation() {
    return this.getNetworkPlayerInfo().getSkinLocation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getCloakLocation() {
    return this.getNetworkPlayerInfo().getCloakLocation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getElytraLocation() {
    return this.getNetworkPlayerInfo().getElytraLocation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasSkin() {
    return this.getNetworkPlayerInfo().hasSkin();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasCloak() {
    return this.getNetworkPlayerInfo().hasCloak();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasElytra() {
    return this.getNetworkPlayerInfo().hasElytra();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isChild() {
    return this.playerEntity.isChild();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getElytraPitch() {
    return this.playerEntity.rotateElytraX;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElytraPitch(float elytraPitch) {
    this.playerEntity.rotateElytraX = elytraPitch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getElytraYaw() {
    return this.playerEntity.rotateElytraY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElytraYaw(float elytraYaw) {
    this.playerEntity.rotateElytraY = elytraYaw;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getElytraRoll() {
    return this.playerEntity.rotateElytraZ;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElytraRoll(float elytraRoll) {
    this.playerEntity.rotateElytraZ = elytraRoll;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSpectator() {
    NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
    return networkPlayerInfo != null && networkPlayerInfo.getGameMode() == GameMode.SPECTATOR;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCreative() {
    NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
    return networkPlayerInfo != null && networkPlayerInfo.getGameMode() == GameMode.CREATIVE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkDespawn() {
    this.playerEntity.checkDespawn();
  }
}
