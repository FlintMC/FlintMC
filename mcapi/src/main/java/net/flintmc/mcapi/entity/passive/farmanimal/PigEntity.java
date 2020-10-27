package net.flintmc.mcapi.entity.passive.farmanimal;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.passive.AnimalEntity;

/** Represents the Minecraft pig entity. */
public interface PigEntity extends AnimalEntity {

  /**
   * Whether the pig entity is saddled.
   *
   * @return {@code true} if the pig entity is saddled, otherwise {@code false}.
   */
  boolean isSaddled();

  /**
   * Changes whether the pig entity is saddled.
   *
   * @param saddled {@code true} if the pig entity should be saddled, otherwise {@code false}.
   */
  void setSaddled(boolean saddled);

  /**
   * Whether the pig entity is boosting.
   *
   * @return {@code true} if the pig entity is boosting, otherwise {@code false}.
   */
  boolean boost();

  /** A factory class for the {@link PigEntity}. */
  @AssistedFactory(PigEntity.class)
  interface Factory {

    /**
     * Creates a new {@link PigEntity} with the given non-null Minecraft entity.
     *
     * @param entity The non-null Minecraft pig entity.
     * @return A created pig entity.
     */
    PigEntity create(@Assisted("entity") Object entity);
  }
}
