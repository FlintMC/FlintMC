package net.labyfy.component.entity.type;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntitySize;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.world.ClientWorld;

public interface EntityType {

  /**
   * Retrieves the classification of this entity type.
   *
   * @return The entity type classification.
   */
  Entity.Classification getClassification();

  boolean isSerializable();

  boolean isSummonable();

  boolean isImmuneToFire();

  boolean canSpawnFarFromPlayer();

  /**
   * Retrieves the size of this entity type.
   *
   * @return The size of this entity type.
   */
  EntitySize getSize();

  /**
   * A factory class for the {@link EntityType}
   */
  @AssistedFactory(EntityType.class)
  interface Factory {

    /**
     * Creates a new {@link EntityType} by the given parameters.
     *
     * @param classification        The classifications for an entity type.
     * @param serializable          Whether the entity type if serializable.
     * @param summonable            Whether the entity type is summonable.
     * @param immuneToFire          Whether the entity type is immune to fire.
     * @param canSpawnFarFromPlayer Whether the entity type can spawn far from player.
     * @param entitySize            The size of the entity type.
     * @return The created entity type.
     */
    EntityType create(
            @Assisted("entityFactory") Entity.Factory entityFactory,
            @Assisted("classification") Entity.Classification classification,
            @Assisted("serializable") boolean serializable,
            @Assisted("summonable") boolean summonable,
            @Assisted("immuneToFire") boolean immuneToFire,
            @Assisted("canSpawnFarFromPlayer") boolean canSpawnFarFromPlayer,
            @Assisted("entitySize") EntitySize entitySize
    );

  }

}
