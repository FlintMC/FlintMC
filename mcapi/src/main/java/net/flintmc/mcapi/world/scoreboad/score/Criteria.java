package net.flintmc.mcapi.world.scoreboad.score;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

/**
 * Represents a Minecraft score criteria.
 */
public interface Criteria {

  /**
   * Retrieves the name of this criteria.
   *
   * @return The name of this criteria.
   */
  String getName();

  /**
   * Whether the criteria is read only.
   *
   * @return {@code true} if the criteria is read only, otherwise {@code false}
   */
  boolean readOnly();

  /**
   * Retrieves the render type of this criteria.
   *
   * @return The render type of this criteria.
   */
  RenderType getRenderType();

  /**
   * A factory class for {@link Criteria}
   */
  @AssistedFactory(Criteria.class)
  interface Factory {

    /**
     * Creates a new {@link Criteria} with the given name.
     *
     * @param name The name of the criteria.
     * @return A created criteria.
     */
    Criteria create(@Assisted("name") String name);

    /**
     * Creates a new {@link Criteria} with the given parameters.
     *
     * @param name       The name of the criteria.
     * @param readOnly   {@code true} if the criteria read only, otherwise {@code false}.
     * @param renderType The render type of the criteria.
     * @return A created criteria.
     */
    Criteria create(
        @Assisted("name") String name,
        @Assisted("readOnly") boolean readOnly,
        @Assisted("renderType") RenderType renderType);
  }
}
