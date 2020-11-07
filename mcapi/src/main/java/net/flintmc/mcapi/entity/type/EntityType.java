package net.flintmc.mcapi.entity.type;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.EntitySize;

/** Represents the entity type. */
public interface EntityType {

  /**
   * Retrieves the classification of this entity type.
   *
   * @return The entity type classification.
   */
  Entity.Classification getClassification();

  /**
   * Whether the entity type is serializable.
   *
   * @return {@code true} if the type is serializable, otherwise {@code false}.
   */
  boolean isSerializable();

  /**
   * Whether the entity type is summonable.
   *
   * @return {@code true} if the type is summonable, otherwise {@code false}.
   */
  boolean isSummonable();

  /**
   * Whether the entity type is immune to fire.
   *
   * @return {@code true} if the type is immune to fire, otherwise {@code false}.
   */
  boolean isImmuneToFire();

  /**
   * Whether the entity type can spawn far from the player.
   *
   * @return {@code true} if the type can spawn far from the player, otherwise {@code false}.
   */
  boolean canSpawnFarFromPlayer();

  /**
   * Retrieves the size of this entity type.
   *
   * @return The size of this entity type.
   */
  EntitySize getSize();

  /** A factory class for the {@link EntityType} */
  @AssistedFactory(EntityType.class)
  interface Factory {

    /**
     * Creates a new {@link EntityType} by the given parameters.
     *
     * @param classification The classifications for an entity type.
     * @param serializable Whether the entity type if serializable.
     * @param summonable Whether the entity type is summonable.
     * @param immuneToFire Whether the entity type is immune to fire.
     * @param canSpawnFarFromPlayer Whether the entity type can spawn far from player.
     * @param entitySize The size of the entity type.
     * @return The created entity type.
     */
    EntityType create(
        @Assisted("classification") Entity.Classification classification,
        @Assisted("serializable") boolean serializable,
        @Assisted("summonable") boolean summonable,
        @Assisted("immuneToFire") boolean immuneToFire,
        @Assisted("canSpawnFarFromPlayer") boolean canSpawnFarFromPlayer,
        @Assisted("entitySize") EntitySize entitySize);
  }
}
