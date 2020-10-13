package net.labyfy.component.entity.type;

import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntitySize;

/**
 * Mapper between the Minecraft entity type and Labyfy entity type.
 */
public interface EntityTypeMapper {

  /**
   * Creates a new Minecraft entity type by using the Labyfy {@link EntityType} as the base.
   *
   * @param type The non-null Labyfy {@link EntityType}.
   * @return The new Minecraft entity type or {@code null} if the given entity type was invalid.
   */
  Object toMinecraftEntityType(EntityType type);

  /**
   * Creates a new {@link EntityType} by using the given Minecraft entity type as the base.
   *
   * @param handle The non-null Minecraft entity type.
   * @return The new Labyfy {@link EntityType} or {@code null} if the given entity type was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity type.
   */
  EntityType fromMinecraftEntityType(Object handle);

  /**
   * Creates a new Minecraft entity classification by using the Labyfy {@link Entity.Classification} as the base.
   *
   * @param classification The non-null Labyfy {@link Entity.Classification}.
   * @return The new Minecraft entity classification or {@code null} if the given entity classification was invalid.
   */
  Object toMinecraftEntityClassification(Entity.Classification classification);

  /**
   * Creates a new {@link Entity.Classification} by using the given Minecraft entity classification as the base.
   *
   * @param handle The non-null Minecraft entity classification.
   * @return The new Labyfy {@link Entity.Classification} or {@code null} if the given entity classification was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity classification.
   */
  Entity.Classification fromMinecraftEntityClassification(Object handle);

  /**
   * Creates a new Minecraft entity size by using the Labyfy {@link EntitySize} as the base.
   *
   * @param entitySize The non-null Labyfy {@link EntitySize}.
   * @return The new Minecraft entity size or {@code null} if the given entity size was invalid.
   */
  Object toMinecraftEntitySize(EntitySize entitySize);

  /**
   * Creates a new {@link EntitySize} by using the given Minecraft entity size as the base.
   *
   * @param handle The non-null Minecraft entity size.
   * @return The new Labyfy {@link EntitySize} or {@code null} if the given entity size was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft entity size.
   */
  EntitySize fromMinecraftEntitySize(Object handle);

}
