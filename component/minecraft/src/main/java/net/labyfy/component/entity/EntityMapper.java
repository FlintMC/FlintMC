package net.labyfy.component.entity;

import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.component.entity.type.EntityPose;
import net.labyfy.component.items.inventory.EquipmentSlotType;
import net.labyfy.component.items.mapper.MinecraftItemMapper;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.player.type.GameMode;
import net.labyfy.component.player.type.hand.HandMapper;
import net.labyfy.component.player.type.sound.SoundMapper;
import net.labyfy.component.resources.ResourceLocationProvider;

public interface EntityMapper {

  EquipmentSlotType fromMinecraftEquipmentSlotType(Object object);

  Object toMinecraftEquipmentSlotType(EquipmentSlotType equipmentSlotType);

  GameMode fromMinecraftGameType(Object object);

  Object toMinecraftGameType(GameMode mode);

  EntityPose fromMinecraftPose(Object object);

  Object toMinecraftPose(EntityPose pose);

  Entity fromMinecraftEntity(Object entity);

  Object toMinecraftEntity(Entity entity);

  PlayerEntity fromMinecraftPlayerEntity(Object entity);

  Object toMinecraftPlayerEntity(PlayerEntity entity);

  HandMapper getHandMapper();

  SoundMapper getSoundMapper();

  MinecraftComponentMapper getComponentMapper();

  MinecraftItemMapper getItemMapper();

  ResourceLocationProvider getResourceLocationProvider();

}
