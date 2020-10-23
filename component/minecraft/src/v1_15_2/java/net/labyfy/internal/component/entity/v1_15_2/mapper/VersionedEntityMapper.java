package net.labyfy.internal.component.entity.v1_15_2.mapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.MobEntity;
import net.labyfy.component.entity.item.ItemEntityMapper;
import net.labyfy.component.entity.mapper.EntityMapper;
import net.labyfy.component.entity.passive.PassiveEntityMapper;
import net.labyfy.component.entity.type.EntityTypeMapper;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.player.RemoteClientPlayer;
import net.labyfy.internal.component.entity.cache.EntityCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.PigEntity;

import java.util.UUID;

@Singleton
@Implement(value = EntityMapper.class, version = "1.15.2")
public class VersionedEntityMapper implements EntityMapper {

  private final EntityCache entityCache;

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
          EntityCache entityCache,
          Entity.Factory entityFactory,
          EntityTypeMapper entityTypeMapper,
          ItemEntityMapper itemEntityMapper,
          LivingEntity.Provider livingEntityProvider,
          MobEntity.Provider mobEntityProvider,
          PassiveEntityMapper passiveEntityMapper,
          PlayerEntity.Provider playerEntityProvider,
          RemoteClientPlayer.Provider remoteClientPlayerProvider) {
    this.entityCache = entityCache;
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
  public Entity fromMinecraftEntity(Object handle) {
    if (!(handle instanceof net.minecraft.entity.Entity)) {
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + net.minecraft.entity.Entity.class.getName());
    }

    net.minecraft.entity.Entity minecraftEntity = (net.minecraft.entity.Entity) handle;
    UUID uniqueId = minecraftEntity.getUniqueID();

    if (minecraftEntity instanceof ItemEntity) {

      return this.entityCache.putIfAbsent(
              uniqueId,
              () -> this.itemEntityMapper.fromMinecraftItemEntity(minecraftEntity)
      );
    } else if (minecraftEntity instanceof PigEntity) {

      return this.entityCache.putIfAbsent(
              uniqueId,
              () -> this.passiveEntityMapper.fromMinecraftPigEntity(minecraftEntity)
      );
    } else if (minecraftEntity instanceof RemoteClientPlayerEntity) {

      return this.entityCache.putIfAbsent(
              uniqueId,
              () -> this.remoteClientPlayerProvider.get(minecraftEntity)
      );
    } else {

      return this.entityCache.putIfAbsent(
              uniqueId,
              () -> this.entityFactory.create(
                      minecraftEntity,
                      this.entityTypeMapper.fromMinecraftEntityType(minecraftEntity.getType())
              )
      );
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
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + net.minecraft.entity.player.PlayerEntity.class.getName());
    }

    net.minecraft.entity.player.PlayerEntity playerEntity = (net.minecraft.entity.player.PlayerEntity) handle;

    return (PlayerEntity) this.entityCache.putIfAbsent(
            playerEntity.getUniqueID(),
            () -> this.playerEntityProvider.get(playerEntity)
    );
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
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + net.minecraft.entity.LivingEntity.class.getName());
    }

    net.minecraft.entity.LivingEntity livingEntity = (net.minecraft.entity.LivingEntity) handle;

    return (LivingEntity) this.entityCache.putIfAbsent(
            livingEntity.getUniqueID(),
            () -> this.livingEntityProvider.get(livingEntity)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftLivingEntity(LivingEntity entity) {

    for (net.minecraft.entity.Entity allEntity : Minecraft.getInstance().world.getAllEntities()) {
      if (allEntity instanceof net.minecraft.entity.LivingEntity && allEntity.getEntityId() == entity.getIdentifier()) {
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
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + net.minecraft.entity.MobEntity.class.getName());
    }

    net.minecraft.entity.MobEntity mobEntity = (net.minecraft.entity.MobEntity) handle;

    return (MobEntity) this.entityCache.putIfAbsent(
            mobEntity.getUniqueID(),
            () -> this.mobEntityProvider.get(mobEntity)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftMobEntity(MobEntity entity) {

    for (net.minecraft.entity.Entity allEntity : Minecraft.getInstance().world.getAllEntities()) {
      if (allEntity instanceof net.minecraft.entity.MobEntity && allEntity.getEntityId() == entity.getIdentifier()) {
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
