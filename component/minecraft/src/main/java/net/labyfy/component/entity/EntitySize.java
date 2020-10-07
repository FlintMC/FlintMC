package net.labyfy.component.entity;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

/**
 * Represents the size of an entity.
 */
public interface EntitySize {

  /**
   * Multiply an entity by the given factor.
   *
   * @param factor The factory to multiply.
   * @return The scaled entity size.
   */
  EntitySize scale(float factor);

  /**
   * Multiply an entity size by the given parameters.
   *
   * @param widthFactor  The width factor to multiply.
   * @param heightFactor The height factor to multiply.
   * @return The scaled entity size.
   */
  EntitySize scale(float widthFactor, float heightFactor);

  /**
   * A factory class for {@link EntitySize}.
   */
  @AssistedFactory(EntitySize.class)
  interface Factory {

    /**
     * Creates a new {@link EntitySize} with the given parameters.
     *
     * @param width  The width of an entity.
     * @param height The height of an entity.
     * @param fixed  Whether the entity is fixed.
     * @return a created entity size.
     */
    EntitySize create(
            @Assisted("width") float width,
            @Assisted("height") float height,
            @Assisted("fixed") boolean fixed
    );

  }

}
