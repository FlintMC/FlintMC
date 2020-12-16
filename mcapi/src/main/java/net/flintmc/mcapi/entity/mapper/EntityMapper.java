package net.flintmc.mcapi.entity.mapper;

import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.MobEntity;
import net.flintmc.mcapi.entity.item.ItemEntityMapper;
import net.flintmc.mcapi.entity.passive.PassiveEntityMapper;
import net.flintmc.mcapi.player.PlayerEntity;

/** Mapper between Minecraft entity and Flint entity. */
public interface EntityMapper {

  /**
   * Creates a new {@link Entity} by using the given Minecraft entity as the base. This method
   * checks whether the entity is a {@link LivingEntity}, {@link PlayerEntity} or {@link MobEntity}
   * and if it is, retrieves one of those.
   *
   * @param handle The non-null Minecraft entity
   * @return The new Flint {@link Entity} or {@code null} if the given entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity.
   */
  Entity fromAnyMinecraftEntity(Object handle);

  /**
   * Creates a new {@link Entity} by using the given Minecraft entity as the base.
   *
   * @param handle The non-null Minecraft entity.
   * @return The new Flint {@link Entity} or {@code null} if the given entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity.
   */
  Entity fromMinecraftEntity(Object handle);

  /**
   * Creates a new Minecraft entity by using the Flint {@link Entity} as the base.
   *
   * @param entity The non-null Flint {@link Entity}.
   * @return The new Minecraft entity or {@code null} if the given entity was invalid.
   */
  Object toMinecraftEntity(Entity entity);

  /**
   * Creates a new {@link Entity} by using the given Minecraft player entity as the base.
   *
   * @param handle The non-null Minecraft player entity.
   * @return The new Flint {@link Entity} or {@code null} if the given player entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft player entity.
   */
  PlayerEntity fromMinecraftPlayerEntity(Object handle);

  /**
   * Creates a new Minecraft player entity by using the Flint {@link PlayerEntity} as the base.
   *
   * @param entity The non-null Flint {@link PlayerEntity}.
   * @return The new Minecraft player entity or {@code null} if the given player entity was invalid.
   */
  Object toMinecraftPlayerEntity(PlayerEntity entity);

  /**
   * Creates a new {@link LivingEntity} by using the given Minecraft living entity as the base.
   *
   * @param handle The non-null Minecraft living entity.
   * @return The new Flint {@link LivingEntity} or {@code null} if the given living entity was
   *     invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft living entity.
   */
  LivingEntity fromMinecraftLivingEntity(Object handle);

  /**
   * Creates a new Minecraft living entity by using the Flint {@link LivingEntity} as the base.
   *
   * @param entity The non-null Flint {@link LivingEntity}.
   * @return The new Minecraft living entity or {@code null} if the given living entity was invalid.
   */
  Object toMinecraftLivingEntity(LivingEntity entity);

  /**
   * Creates a new {@link MobEntity} by using the given Minecraft mob entity as the base.
   *
   * @param handle The non-null Minecraft living entity.
   * @return The new Flint {@link MobEntity} or {@code null} if the given mob entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft mob entity.
   */
  MobEntity fromMinecraftMobEntity(Object handle);

  /**
   * Creates a new Minecraft mob entity by using the Flint {@link MobEntity} as the base.
   *
   * @param entity The non-null Flint {@link MobEntity}.
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
