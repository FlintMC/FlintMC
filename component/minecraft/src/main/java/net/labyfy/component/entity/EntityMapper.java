package net.labyfy.component.entity;

import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.component.entity.type.EntityPose;
import net.labyfy.component.items.inventory.EquipmentSlotType;
import net.labyfy.component.items.mapper.MinecraftItemMapper;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.player.util.GameMode;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.player.util.sound.SoundCategory;
import net.labyfy.component.resources.ResourceLocationProvider;

public interface EntityMapper {

  EquipmentSlotType fromMinecraftEquipmentSlotType(Object object);

  Object toMinecraftEquipmentSlotType(EquipmentSlotType equipmentSlotType);

  Hand fromMinecraftHand(Object object);

  Object toMinecraftHand(Hand hand);

  Hand.Side fromMinecraftHandSide(Object object);

  Object toMinecraftHandSide(Hand.Side hideSide);

  Sound fromMinecraftSound(Object object);

  Object toMinecraftSoundEvent(Sound sound);

  SoundCategory fromMinecraftSoundCategory(Object object);

  Object toMinecraftSoundCategory(SoundCategory category);

  GameMode fromMinecraftGameType(Object object);

  Object toMinecraftGameType(GameMode mode);

  EntityPose fromMinecraftPose(Object object);

  Object toMinecraftPose(EntityPose pose);

  Entity fromMinecraftEntity(Object entity);

  Object toMinecraftEntity(Entity entity);

  PlayerEntity fromMinecraftPlayerEntity(Object entity);

  Object toMinecraftPlayerEntity(PlayerEntity entity);

  MinecraftComponentMapper getComponentMapper();

  MinecraftItemMapper getItemMapper();

  ResourceLocationProvider getResourceLocationProvider();

}
