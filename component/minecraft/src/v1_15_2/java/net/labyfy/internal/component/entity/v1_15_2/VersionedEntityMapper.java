package net.labyfy.internal.component.entity.v1_15_2;

import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.item.ItemEntity;
import net.labyfy.component.entity.MobEntity;
import net.labyfy.component.entity.type.EntityPose;
import net.labyfy.component.entity.type.EntityTypeRegister;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.inventory.EquipmentSlotType;
import net.labyfy.component.items.mapper.MinecraftItemMapper;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.player.type.GameMode;
import net.labyfy.component.player.type.hand.HandMapper;
import net.labyfy.component.player.type.sound.SoundMapper;
import net.labyfy.component.resources.ResourceLocationProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameType;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 1.15.2 implementation of the {@link EntityMapper}.
 */
@Singleton
@Implement(value = EntityMapper.class, version = "1.15.2")
public class VersionedEntityMapper implements EntityMapper {

  private final MinecraftItemMapper itemMapper;
  private final MinecraftComponentMapper componentMapper;
  private final ResourceLocationProvider resourceLocationProvider;
  private final SoundMapper soundMapper;
  private final HandMapper handMapper;

  private final Entity.Factory entityFactory;
  private final ItemEntity.Factory itemEntityFactory;
  private final LivingEntity.Provider livingEntityProvider;
  private final MobEntity.Provider mobEntityProvider;
  private final PlayerEntity.Provider playerEntityProvider;
  private final EntityTypeRegister entityTypeRegister;

  @Inject
  private VersionedEntityMapper(
          MinecraftItemMapper itemMapper,
          MinecraftComponentMapper componentMapper,
          ResourceLocationProvider resourceLocationProvider,
          SoundMapper soundMapper,
          HandMapper handMapper,
          Entity.Factory entityFactory,
          MobEntity.Provider mobEntityProvider,
          ItemEntity.Factory itemEntityFactory,
          LivingEntity.Provider livingEntityProvider,
          PlayerEntity.Provider playerEntityProvider,
          EntityTypeRegister entityTypeRegister
  ) {
    this.itemMapper = itemMapper;
    this.componentMapper = componentMapper;
    this.resourceLocationProvider = resourceLocationProvider;
    this.soundMapper = soundMapper;
    this.handMapper = handMapper;
    this.entityFactory = entityFactory;
    this.mobEntityProvider = mobEntityProvider;
    this.itemEntityFactory = itemEntityFactory;
    this.livingEntityProvider = livingEntityProvider;
    this.playerEntityProvider = playerEntityProvider;
    this.entityTypeRegister = entityTypeRegister;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EquipmentSlotType fromMinecraftEquipmentSlotType(Object handle) {
    if (!(handle instanceof net.minecraft.inventory.EquipmentSlotType)) {
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + net.minecraft.inventory.EquipmentSlotType.class.getName());
    }

    net.minecraft.inventory.EquipmentSlotType equipmentSlotType = (net.minecraft.inventory.EquipmentSlotType) handle;

    switch (equipmentSlotType) {

      case MAINHAND:
        return EquipmentSlotType.MAIN_HAND;
      case OFFHAND:
        return EquipmentSlotType.OFF_HAND;
      case FEET:
        return EquipmentSlotType.FEET;
      case LEGS:
        return EquipmentSlotType.LEGS;
      case CHEST:
        return EquipmentSlotType.CHEST;
      case HEAD:
        return EquipmentSlotType.HEAD;
      default:
        throw new IllegalStateException("Unexpected value: " + equipmentSlotType);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftEquipmentSlotType(EquipmentSlotType equipmentSlotType) {
    switch (equipmentSlotType) {
      case MAIN_HAND:
        return net.minecraft.inventory.EquipmentSlotType.MAINHAND;
      case OFF_HAND:
        return net.minecraft.inventory.EquipmentSlotType.OFFHAND;
      case FEET:
        return net.minecraft.inventory.EquipmentSlotType.FEET;
      case LEGS:
        return net.minecraft.inventory.EquipmentSlotType.LEGS;
      case CHEST:
        return net.minecraft.inventory.EquipmentSlotType.CHEST;
      case HEAD:
        return net.minecraft.inventory.EquipmentSlotType.HEAD;
      default:
        throw new IllegalStateException("Unexpected value: " + equipmentSlotType);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameMode fromMinecraftGameType(Object handle) {
    if (!(handle instanceof GameType)) {
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + GameType.class.getName());
    }

    GameType gameType = (GameType) handle;

    switch (gameType) {
      case NOT_SET:
      case SURVIVAL:
        return GameMode.SURVIVAL;
      case CREATIVE:
        return GameMode.CREATIVE;
      case ADVENTURE:
        return GameMode.ADVENTURE;
      case SPECTATOR:
        return GameMode.SPECTATOR;
      default:
        throw new IllegalStateException("Unexpected value: " + gameType);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftGameType(GameMode mode) {
    switch (mode) {
      case SURVIVAL:
        return GameType.SURVIVAL;
      case CREATIVE:
        return GameType.CREATIVE;
      case ADVENTURE:
        return GameType.ADVENTURE;
      case SPECTATOR:
        return GameType.SPECTATOR;
      default:
        return GameType.NOT_SET;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityPose fromMinecraftPose(Object handle) {
    if (!(handle instanceof Pose)) {
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + Pose.class.getName());
    }

    Pose pose = (Pose) handle;

    switch (pose) {
      case STANDING:
        return EntityPose.STANDING;
      case FALL_FLYING:
        return EntityPose.FALL_FLYING;
      case SLEEPING:
        return EntityPose.SLEEPING;
      case SWIMMING:
        return EntityPose.SWIMMING;
      case SPIN_ATTACK:
        return EntityPose.SPIN_ATTACK;
      case CROUCHING:
        return EntityPose.CROUCHING;
      case DYING:
        return EntityPose.DYING;
      default:
        throw new IllegalStateException("Unexpected value: " + pose);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftPose(EntityPose pose) {
    switch (pose) {
      case STANDING:
        return Pose.STANDING;
      case FALL_FLYING:
        return Pose.FALL_FLYING;
      case SLEEPING:
        return Pose.SLEEPING;
      case SWIMMING:
        return Pose.SWIMMING;
      case SPIN_ATTACK:
        return Pose.SPIN_ATTACK;
      case CROUCHING:
        return Pose.CROUCHING;
      case DYING:
        return Pose.DYING;
      default:
        throw new IllegalStateException("Unexpected value: " + pose);
    }

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
            this.entityTypeRegister.getEntityType(
                    Registry.ENTITY_TYPE.getKey(minecraftEntity.getType()).getPath()
            )
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
      if (allEntity instanceof net.minecraft.entity.LivingEntity) {
        net.minecraft.entity.LivingEntity livingEntity = (net.minecraft.entity.LivingEntity) allEntity;

        if (livingEntity.getEntityId() == entity.getIdentifier()) {
          return livingEntity;
        }

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
      if (allEntity instanceof net.minecraft.entity.item.ItemEntity) {
        net.minecraft.entity.item.ItemEntity livingEntity = (net.minecraft.entity.item.ItemEntity) allEntity;

        if (livingEntity.getEntityId() == itemEntity.getIdentifier()) {
          return livingEntity;
        }

      }

    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandMapper getHandMapper() {
    return this.handMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SoundMapper getSoundMapper() {
    return this.soundMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MinecraftComponentMapper getComponentMapper() {
    return this.componentMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MinecraftItemMapper getItemMapper() {
    return this.itemMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocationProvider getResourceLocationProvider() {
    return this.resourceLocationProvider;
  }
}
