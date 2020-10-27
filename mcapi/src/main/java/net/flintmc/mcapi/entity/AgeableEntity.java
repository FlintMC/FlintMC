package net.flintmc.mcapi.entity;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.type.hand.Hand;

/** Represents the Minecraft ageable entity. */
public interface AgeableEntity extends CreatureEntity {

  /**
   * Whether the entity can process interactively.
   *
   * @param entity The player entity that interacts.
   * @param hand The hand that interacts.
   * @return {@code true} if the entity can process interactively, otherwise {@code false}.
   */
  boolean processInteract(PlayerEntity entity, Hand hand);

  /**
   * Retrieves the growing age of this ageable entity.
   *
   * @return The growing age.
   */
  int getGrowingAge();

  /**
   * Changes the growing age of this ageable entity.
   *
   * @param age The new age.
   */
  void setGrowingAge(int age);

  /**
   * Ages up the entity.
   *
   * @param growth The growth in seconds.
   * @param updateForcedAge {@code true} to update the forced age, otherwise {@code false}.
   */
  void ageUp(int growth, boolean updateForcedAge);

  /**
   * Adds growth to the ageable entity.
   *
   * @param growth The growth to be added.
   */
  void addGrowth(int growth);

  @AssistedFactory(AgeableEntity.class)
  interface Factory {

    /**
     * Creates a new {@link AgeableEntity} with the given parameters.
     *
     * @param entity The entity.
     * @param entityType The type of the entity.
     * @return A created ageable entity.
     */
    AgeableEntity create(
        @Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);
  }

  /** Service interface for creating {@link AgeableEntity}'s. */
  interface Provider {

    /**
     * Creates a new {@link AgeableEntity} with the given parameters.
     *
     * @param entity The entity.
     * @return A created ageable entity.
     */
    AgeableEntity get(Object entity);
  }
}
