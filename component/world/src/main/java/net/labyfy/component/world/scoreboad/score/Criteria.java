package net.labyfy.component.world.scoreboad.score;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.world.scoreboad.type.RenderType;

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

  @AssistedFactory(Criteria.class)
  interface Factory {

    Criteria create(@Assisted("name") String name);

    Criteria create(@Assisted("name") String name, @Assisted("readOnly") boolean readOnly, @Assisted("renderType") RenderType renderType);

  }

  interface Provider {

    Criteria get(String name);

    Criteria get(String name, boolean readOnly, RenderType renderType);

  }

}
