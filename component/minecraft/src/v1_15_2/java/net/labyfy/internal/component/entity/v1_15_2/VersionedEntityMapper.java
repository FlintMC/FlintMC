package net.labyfy.internal.component.entity.v1_15_2;

import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntityMapper;
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
import net.minecraft.entity.Pose;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameType;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Implement(value = EntityMapper.class, version = "1.15.2")
public class VersionedEntityMapper implements EntityMapper {

  private final MinecraftItemMapper itemMapper;
  private final MinecraftComponentMapper componentMapper;
  private final ResourceLocationProvider resourceLocationProvider;
  private final SoundMapper soundMapper;
  private final HandMapper handMapper;

  private final Entity.Provider entityProvider;
  private final EntityTypeRegister entityTypeRegister;

  @Inject
  private VersionedEntityMapper(
          MinecraftItemMapper itemMapper,
          MinecraftComponentMapper componentMapper,
          ResourceLocationProvider resourceLocationProvider,
          SoundMapper soundMapper,
          HandMapper handMapper,
          Entity.Provider entityProvider,
          EntityTypeRegister entityTypeRegister
  ) {
    this.itemMapper = itemMapper;
    this.componentMapper = componentMapper;
    this.resourceLocationProvider = resourceLocationProvider;
    this.soundMapper = soundMapper;
    this.handMapper = handMapper;
    this.entityProvider = entityProvider;
    this.entityTypeRegister = entityTypeRegister;
  }

  @Override
  public EquipmentSlotType fromMinecraftEquipmentSlotType(Object object) {
    if (!(object instanceof net.minecraft.inventory.EquipmentSlotType)) {
      throw new IllegalArgumentException("");
    }

    net.minecraft.inventory.EquipmentSlotType equipmentSlotType = (net.minecraft.inventory.EquipmentSlotType) object;

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

  @Override
  public GameMode fromMinecraftGameType(Object object) {
    if (!(object instanceof GameType)) {
      throw new IllegalArgumentException("");
    }

    GameType gameType = (GameType) object;

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

  @Override
  public EntityPose fromMinecraftPose(Object object) {
    if (!(object instanceof Pose)) {
      throw new IllegalArgumentException("");
    }

    Pose pose = (Pose) object;

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

  @Override
  public Entity fromMinecraftEntity(Object entity) {
    if (!(entity instanceof net.minecraft.entity.Entity)) {
      throw new IllegalArgumentException("");
    }

    net.minecraft.entity.Entity minecraftEntity = (net.minecraft.entity.Entity) entity;

    return this.entityProvider.get(
            minecraftEntity,
            this.entityTypeRegister.getEntityType(
                    Registry.ENTITY_TYPE.getKey(minecraftEntity.getType()).getPath()
            )
    );
  }

  @Override
  public Object toMinecraftEntity(Entity entity) {
    return null;
  }

  @Override
  public PlayerEntity fromMinecraftPlayerEntity(Object entity) {
    if (!(entity instanceof net.minecraft.entity.player.PlayerEntity)) {
      throw new IllegalArgumentException("");
    }

    net.minecraft.entity.player.PlayerEntity playerEntity = (net.minecraft.entity.player.PlayerEntity) entity;

    return null;
  }

  @Override
  public Object toMinecraftPlayerEntity(PlayerEntity entity) {
    return null;
  }

  @Override
  public HandMapper getHandMapper() {
    return this.handMapper;
  }

  @Override
  public SoundMapper getSoundMapper() {
    return this.soundMapper;
  }

  @Override
  public MinecraftComponentMapper getComponentMapper() {
    return this.componentMapper;
  }

  @Override
  public MinecraftItemMapper getItemMapper() {
    return this.itemMapper;
  }

  @Override
  public ResourceLocationProvider getResourceLocationProvider() {
    return this.resourceLocationProvider;
  }
}
