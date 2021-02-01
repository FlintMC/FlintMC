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

package net.flintmc.mcapi.v1_16_5.entity.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.UUID;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.EntityRepository;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.MobEntity;
import net.flintmc.mcapi.entity.item.ItemEntityMapper;
import net.flintmc.mcapi.entity.mapper.EntityMapper;
import net.flintmc.mcapi.entity.passive.PassiveEntityMapper;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.RemoteClientPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.PigEntity;

@Singleton
@Implement(value = EntityMapper.class, version = "1.16.5")
public class VersionedEntityMapper implements EntityMapper {

  private final EntityRepository entityCache;

  private final Entity.Factory entityFactory;
  private final EntityTypeMapper entityTypeMapper;

  private final LivingEntity.Provider livingEntityProvider;
  private final MobEntity.Provider mobEntityProvider;
  private final ItemEntityMapper itemEntityMapper;
  private final PassiveEntityMapper passiveEntityMapper;
  private final PlayerEntity.Provider playerEntityProvider;
  private final RemoteClientPlayer.Provider remoteClientPlayerProvider;

  @Inject
  private VersionedEntityMapper(
      EntityRepository entityRepository,
      Entity.Factory entityFactory,
      EntityTypeMapper entityTypeMapper,
      ItemEntityMapper itemEntityMapper,
      LivingEntity.Provider livingEntityProvider,
      MobEntity.Provider mobEntityProvider,
      PassiveEntityMapper passiveEntityMapper,
      PlayerEntity.Provider playerEntityProvider,
      RemoteClientPlayer.Provider remoteClientPlayerProvider) {
    this.entityCache = entityRepository;
    this.entityFactory = entityFactory;
    this.livingEntityProvider = livingEntityProvider;
    this.mobEntityProvider = mobEntityProvider;
    this.playerEntityProvider = playerEntityProvider;
    this.entityTypeMapper = entityTypeMapper;
    this.itemEntityMapper = itemEntityMapper;
    this.passiveEntityMapper = passiveEntityMapper;
    this.remoteClientPlayerProvider = remoteClientPlayerProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Entity fromAnyMinecraftEntity(Object handle) {
    if (handle instanceof net.minecraft.entity.player.PlayerEntity) {
      return this.fromMinecraftPlayerEntity(handle);
    }

    if (handle instanceof net.minecraft.entity.MobEntity) {
      return this.fromMinecraftMobEntity(handle);
    }

    if (handle instanceof net.minecraft.entity.LivingEntity) {
      return this.fromMinecraftLivingEntity(handle);
    }

    return this.fromMinecraftEntity(handle);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Entity fromMinecraftEntity(Object handle) {
    if (!(handle instanceof net.minecraft.entity.Entity)) {
      throw new IllegalArgumentException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.Entity.class.getName());
    }

    net.minecraft.entity.Entity minecraftEntity = (net.minecraft.entity.Entity) handle;
    UUID uniqueId = minecraftEntity.getUniqueID();

    if (minecraftEntity instanceof ItemEntity) {

      return this.entityCache.putIfAbsent(
          uniqueId, () -> this.itemEntityMapper.fromMinecraftItemEntity(minecraftEntity));
    } else if (minecraftEntity instanceof PigEntity) {

      return this.entityCache.putIfAbsent(
          uniqueId, () -> this.passiveEntityMapper.fromMinecraftPigEntity(minecraftEntity));
    } else if (minecraftEntity instanceof RemoteClientPlayerEntity) {

      return this.entityCache.putIfAbsent(
          uniqueId, () -> this.remoteClientPlayerProvider.get(minecraftEntity));
    } else {

      return this.entityCache.putIfAbsent(
          uniqueId,
          () ->
              this.entityFactory.create(
                  minecraftEntity,
                  this.entityTypeMapper.fromMinecraftEntityType(minecraftEntity.getType())));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftEntity(Entity entity) {
    for (net.minecraft.entity.Entity allEntity : Minecraft.getInstance().world.getAllEntities()) {
      if (allEntity.getUniqueID().equals(entity.getUniqueId())) {
        return allEntity;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PlayerEntity fromMinecraftPlayerEntity(Object handle) {
    if (!(handle instanceof net.minecraft.entity.player.PlayerEntity)) {
      throw new IllegalArgumentException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.player.PlayerEntity.class.getName());
    }

    net.minecraft.entity.player.PlayerEntity playerEntity =
        (net.minecraft.entity.player.PlayerEntity) handle;

    return (PlayerEntity)
        this.entityCache.putIfAbsent(
            playerEntity.getUniqueID(), () -> this.playerEntityProvider.get(playerEntity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftPlayerEntity(PlayerEntity entity) {

    for (AbstractClientPlayerEntity player : Minecraft.getInstance().world.getPlayers()) {
      if (player.getGameProfile().getId().equals(entity.getGameProfile().getUniqueId())) {
        return player;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LivingEntity fromMinecraftLivingEntity(Object handle) {
    if (!(handle instanceof net.minecraft.entity.LivingEntity)) {
      throw new IllegalArgumentException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.LivingEntity.class.getName());
    }

    net.minecraft.entity.LivingEntity livingEntity = (net.minecraft.entity.LivingEntity) handle;

    return (LivingEntity)
        this.entityCache.putIfAbsent(
            livingEntity.getUniqueID(), () -> this.livingEntityProvider.get(livingEntity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftLivingEntity(LivingEntity entity) {

    for (net.minecraft.entity.Entity allEntity : Minecraft.getInstance().world.getAllEntities()) {
      if (allEntity instanceof net.minecraft.entity.LivingEntity
          && allEntity.getEntityId() == entity.getIdentifier()) {
        return allEntity;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MobEntity fromMinecraftMobEntity(Object handle) {
    if (!(handle instanceof net.minecraft.entity.MobEntity)) {
      throw new IllegalArgumentException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.MobEntity.class.getName());
    }

    net.minecraft.entity.MobEntity mobEntity = (net.minecraft.entity.MobEntity) handle;

    return (MobEntity)
        this.entityCache.putIfAbsent(
            mobEntity.getUniqueID(), () -> this.mobEntityProvider.get(mobEntity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftMobEntity(MobEntity entity) {

    for (net.minecraft.entity.Entity allEntity : Minecraft.getInstance().world.getAllEntities()) {
      if (allEntity instanceof net.minecraft.entity.MobEntity
          && allEntity.getEntityId() == entity.getIdentifier()) {
        return allEntity;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemEntityMapper getItemEntityMapper() {
    return this.itemEntityMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PassiveEntityMapper getPassiveEntityMapper() {
    return this.passiveEntityMapper;
  }
}
