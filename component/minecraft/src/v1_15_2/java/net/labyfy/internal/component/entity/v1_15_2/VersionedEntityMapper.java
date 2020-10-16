package net.labyfy.internal.component.entity.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.MobEntity;
import net.labyfy.component.entity.item.ItemEntity;
import net.labyfy.component.entity.mapper.EntityMapper;
import net.labyfy.component.entity.type.EntityTypeMapper;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.PlayerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;

@Singleton
@Implement(value = EntityMapper.class, version = "1.15.2")
public class VersionedEntityMapper implements EntityMapper {

  private final Entity.Factory entityFactory;
  private final ItemEntity.Factory itemEntityFactory;
  private final LivingEntity.Provider livingEntityProvider;
  private final MobEntity.Provider mobEntityProvider;
  private final PlayerEntity.Provider playerEntityProvider;
  private final EntityTypeMapper entityTypeMapper;

  @Inject
  private VersionedEntityMapper(
          Entity.Factory entityFactory,
          ItemEntity.Factory itemEntityFactory,
          LivingEntity.Provider livingEntityProvider,
          MobEntity.Provider mobEntityProvider,
          PlayerEntity.Provider playerEntityProvider,
          EntityTypeMapper entityTypeMapper
  ) {
    this.entityFactory = entityFactory;
    this.itemEntityFactory = itemEntityFactory;
    this.livingEntityProvider = livingEntityProvider;
    this.mobEntityProvider = mobEntityProvider;
    this.playerEntityProvider = playerEntityProvider;
    this.entityTypeMapper = entityTypeMapper;
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

    return this.entityFactory.create(
            minecraftEntity,
            this.entityTypeMapper.fromMinecraftEntityType(minecraftEntity.getType())
    );
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

    return this.playerEntityProvider.get(playerEntity);
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

    net.minecraft.entity.LivingEntity entity = (net.minecraft.entity.LivingEntity) handle;

    return this.livingEntityProvider.get(entity);
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

    return this.mobEntityProvider.get(mobEntity);
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
  public ItemEntity fromMinecraftItemEntity(Object handle) {
    if (!(handle instanceof net.minecraft.entity.item.ItemEntity)) {
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + net.minecraft.entity.item.ItemEntity.class.getName());
    }

    net.minecraft.entity.item.ItemEntity itemEntity = (net.minecraft.entity.item.ItemEntity) handle;

    return this.itemEntityFactory.create(itemEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftItemEntity(ItemEntity itemEntity) {

    for (net.minecraft.entity.Entity allEntity : Minecraft.getInstance().world.getAllEntities()) {
      if (allEntity instanceof net.minecraft.entity.item.ItemEntity && allEntity.getEntityId() == itemEntity.getIdentifier()) {
        return allEntity;
      }

    }

    return null;
  }

}
