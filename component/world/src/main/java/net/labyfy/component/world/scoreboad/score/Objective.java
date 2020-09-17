package net.labyfy.component.world.scoreboad.score;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.world.scoreboad.type.RenderType;

/**
 * Represents a Minecraft score objective
 */
public interface Objective {

  /**
   * Retrieves the registry name of this objective.
   *
   * @return The registry name of this objective
   */
  String getName();

  /**
   * Retrieves the display name of this objective
   *
   * @return The display name of this objective
   */
  ChatComponent getDisplayName();

  /**
   * Retrieves the criteria of this objective
   *
   * @return The criteria of this objective
   */
  Criteria getCriteria();

  /**
   * Retrieves the render type of this objective
   *
   * @return The render type of this objective
   */
  RenderType getRenderType();

  @AssistedFactory(Objective.class)
  interface Factory {

    Objective create(
            @Assisted("name") String name,
            @Assisted("displayName") ChatComponent displayName,
            @Assisted("criteria") Criteria criteria,
            @Assisted("renderType") RenderType type
    );

  }

  interface Provider {

    Objective get(String name, ChatComponent displayName, Criteria criteria, RenderType type);

  }

}
