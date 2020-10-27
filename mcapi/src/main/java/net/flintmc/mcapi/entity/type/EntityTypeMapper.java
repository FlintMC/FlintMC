package net.flintmc.mcapi.entity.type;

import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.EntitySize;

/** Mapper between the Minecraft entity type and Flint entity type. */
public interface EntityTypeMapper {

  /**
   * Creates a new {@link EntityType} by using the given Minecraft entity type as the base.
   *
   * @param handle The non-null Minecraft entity type.
   * @return The new Flint {@link EntityType} or {@code null} if the given entity type was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity type.
   */
  EntityType fromMinecraftEntityType(Object handle);

  /**
   * Creates a new Minecraft entity classification by using the Flint {@link Entity.Classification}
   * as the base.
   *
   * @param classification The non-null Flint {@link Entity.Classification}.
   * @return The new Minecraft entity classification or {@code null} if the given entity
   *     classification was invalid.
   */
  Object toMinecraftEntityClassification(Entity.Classification classification);

  /**
   * Creates a new {@link Entity.Classification} by using the given Minecraft entity classification
   * as the base.
   *
   * @param handle The non-null Minecraft entity classification.
   * @return The new Flint {@link Entity.Classification} or {@code null} if the given entity
   *     classification was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity classification.
   */
  Entity.Classification fromMinecraftEntityClassification(Object handle);

  /**
   * Creates a new Minecraft entity size by using the Flint {@link EntitySize} as the base.
   *
   * @param entitySize The non-null Flint {@link EntitySize}.
   * @return The new Minecraft entity size or {@code null} if the given entity size was invalid.
   */
  Object toMinecraftEntitySize(EntitySize entitySize);

  /**
   * Creates a new {@link EntitySize} by using the given Minecraft entity size as the base.
   *
   * @param handle The non-null Minecraft entity size.
   * @return The new Flint {@link EntitySize} or {@code null} if the given entity size was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity size.
   */
  EntitySize fromMinecraftEntitySize(Object handle);
}
