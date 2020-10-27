package net.labyfy.component.entity.passive;

import net.labyfy.component.entity.passive.farmanimal.PigEntity;

/**
 * Mapper between the Minecraft passive entities and Labyfy passive entities.
 */
public interface PassiveEntityMapper {

  /**
   * Creates a new {@link AmbientEntity} by using the given Minecraft ambient entity as the base.
   *
   * @param handle The non-null Minecraft ambient entity.
   * @return The new Labyfy {@link AmbientEntity} or {@code null} if the given ambient entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft ambient entity.
   */
  AmbientEntity fromMinecraftAmbientEntity(Object handle);

  /**
   * Creates a new Minecraft ambient entity by using the Labyfy {@link AmbientEntity} as the base.
   *
   * @param ambientEntity The non-null Labyfy {@link AmbientEntity}.
   * @return The new Minecraft ambient entity or {@code null} if the given ambient entity was invalid.
   */
  Object toMinecraftAmbientEntity(AmbientEntity ambientEntity);

  /**
   * Creates a new {@link AnimalEntity} by using the given Minecraft animal entity as the base.
   *
   * @param handle The non-null Minecraft animal entity.
   * @return The new Labyfy {@link AnimalEntity} or {@code null} if the given animal entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft animal entity.
   */
  AnimalEntity fromMinecraftAnimalEntity(Object handle);

  /**
   * Creates a new Minecraft animal entity by using the Labyfy {@link AnimalEntity} as the base.
   *
   * @param animalEntity The non-null Labyfy {@link AnimalEntity}.
   * @return The new Minecraft animal entity or {@code null} if the given animal entity was invalid.
   */
  Object toMinecraftAnimalEntity(AnimalEntity animalEntity);


  /**
   * Creates a new {@link PigEntity} by using the given Minecraft pig entity as the base.
   *
   * @param handle The non-null Minecraft pig entity.
   * @return The new Labyfy {@link PigEntity} or {@code null} if the given pig entity was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft pig entity.
   */
  PigEntity fromMinecraftPigEntity(Object handle);

  /**
   * Creates a new Minecraft pig entity by using the Labyfy {@link PigEntity} as the base.
   *
   * @param pigEntity The non-null Labyfy {@link PigEntity}.
   * @return The new Minecraft pig entity or {@code null} if the given pig entity was invalid.
   */
  Object toMinecraftPigEntity(PigEntity pigEntity);

}
