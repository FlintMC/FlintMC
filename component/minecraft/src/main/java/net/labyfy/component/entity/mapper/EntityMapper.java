package net.labyfy.component.entity.mapper;

import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.MobEntity;
import net.labyfy.component.entity.item.ItemEntityMapper;
import net.labyfy.component.entity.passive.PassiveEntityMapper;
import net.labyfy.component.player.PlayerEntity;

/**
 * Mapper between Minecraft entity and Labyfy entity.
 */
public interface EntityMapper {

  /**
   * Creates a new {@link Entity} by using the given Minecraft entity as the base.
   *
   * @param handle The non-null Minecraft entity.
   * @return The new Labyfy {@link Entity} or {@code null} if the given entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity.
   */
  Entity fromMinecraftEntity(Object handle);

  /**
   * Creates a new Minecraft entity by using the Labyfy {@link Entity} as the base.
   *
   * @param entity The non-null Labyfy {@link Entity}.
   * @return The new Minecraft entity or {@code null} if the given entity was invalid.
   */
  Object toMinecraftEntity(Entity entity);

  /**
   * Creates a new {@link Entity} by using the given Minecraft player entity as the base.
   *
   * @param handle The non-null Minecraft player entity.
   * @return The new Labyfy {@link Entity} or {@code null} if the given player entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft player entity.
   */
  PlayerEntity fromMinecraftPlayerEntity(Object handle);

  /**
   * Creates a new Minecraft player entity by using the Labyfy {@link PlayerEntity} as the base.
   *
   * @param entity The non-null Labyfy {@link PlayerEntity}.
   * @return The new Minecraft player entity or {@code null} if the given player entity was invalid.
   */
  Object toMinecraftPlayerEntity(PlayerEntity entity);

  /**
   * Creates a new {@link LivingEntity} by using the given Minecraft living entity as the base.
   *
   * @param handle The non-null Minecraft living entity.
   * @return The new Labyfy {@link LivingEntity} or {@code null} if the given living entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft living entity.
   */
  LivingEntity fromMinecraftLivingEntity(Object handle);

  /**
   * Creates a new Minecraft living entity by using the Labyfy {@link LivingEntity} as the base.
   *
   * @param entity The non-null Labyfy {@link LivingEntity}.
   * @return The new Minecraft living entity or {@code null} if the given living entity was invalid.
   */
  Object toMinecraftLivingEntity(LivingEntity entity);

  /**
   * Creates a new {@link MobEntity} by using the given Minecraft mob entity as the base.
   *
   * @param handle The non-null Minecraft living entity.
   * @return The new Labyfy {@link MobEntity} or {@code null} if the given mob entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft mob entity.
   */
  MobEntity fromMinecraftMobEntity(Object handle);

  /**
   * Creates a new Minecraft mob entity by using the Labyfy {@link MobEntity} as the base.
   *
   * @param entity The non-null Labyfy {@link MobEntity}.
   * @return The new Minecraft mob entity or {@code null} if the given mob entity was invalid.
   */
  Object toMinecraftMobEntity(MobEntity entity);

  /**
   * Retrieves the item entity mapper.
   *
   * @return The item entity mapper.
   */
  ItemEntityMapper getItemEntityMapper();

  /**
   * Retrieves the passive entity mapper.
   *
   * @return The passive entity mapper.
   */
  PassiveEntityMapper getPassiveEntityMapper();
}
