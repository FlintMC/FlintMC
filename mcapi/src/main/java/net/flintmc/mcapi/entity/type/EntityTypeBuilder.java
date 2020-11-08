package net.flintmc.mcapi.entity.type;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;

/** Represents a builder to built entity types. */
public interface EntityTypeBuilder {

  /**
   * Sets the size of the entity type.
   *
   * @param width The width of the entity type.
   * @param height The height of the entity type.
   * @return This builder, for chaining.
   */
  EntityTypeBuilder size(float width, float height);

  /**
   * Disables summoning for the entity type.
   *
   * @return This builder, for chaining.
   */
  EntityTypeBuilder disableSummoning();

  /**
   * Disables serialization for the entity type.
   *
   * @return This builder, for chaining.
   */
  EntityTypeBuilder disableSerialization();

  /**
   * Enables immune to fire for the entity type.
   *
   * @return This builder, for chaining.
   */
  EntityTypeBuilder immuneToFire();

  /**
   * Enables can spawn far from the player for the entity type.
   *
   * @return This builder, for chaining.
   */
  EntityTypeBuilder canSpawnFarFromPlayer();

  /**
   * Built a new entity type.
   *
   * @param id The identifier of the entity type.
   * @return The built entity type.
   */
  EntityType build(String id);

  /** A factory class for {@link EntityTypeBuilder}. */
  @AssistedFactory(EntityTypeBuilder.class)
  interface Factory {

    /**
     * Creates a new {@link EntityTypeBuilder} with the given {@link Entity.Classification}.
     *
     * @param classification The classification for the entity builder.
     * @return A created entity type builder.
     */
    EntityTypeBuilder create(@Assisted("classification") Entity.Classification classification);
  }
}
