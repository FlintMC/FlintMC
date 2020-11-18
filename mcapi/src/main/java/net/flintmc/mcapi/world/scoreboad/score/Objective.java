package net.flintmc.mcapi.world.scoreboad.score;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

/** Represents a Minecraft score objective */
public interface Objective {

  /**
   * Retrieves the scoreboard of this objective.
   *
   * @return The objective's scoreboard.
   */
  Scoreboard getScoreboard();

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

  /** A factory class for {@link Objective} */
  @AssistedFactory(Objective.class)
  interface Factory {

    /**
     * Creates a new {@link Objective} with the given parameters.
     *
     * @param scoreboard The scoreboard for this objective.
     * @param name The registry name for this objective.
     * @param displayName The name that is displayed.
     * @param criteria The criteria for this objective.
     * @param type The render type for this objective.
     * @return A created objective.
     */
    Objective create(
        @Assisted("scoreboard") Scoreboard scoreboard,
        @Assisted("name") String name,
        @Assisted("displayName") ChatComponent displayName,
        @Assisted("criteria") Criteria criteria,
        @Assisted("renderType") RenderType type);
  }

  /** Represents a service interface for creating {@link Objective} */
  interface Provider {

    /**
     * Creates a new {@link Objective} with the given parameters.
     *
     * @param scoreboard The scoreboard for this objective.
     * @param name The registry name for this objective.
     * @param displayName The name that is displayed.
     * @param criteria The criteria for this objective.
     * @param type The render type for this objective.
     * @return A created objective.
     */
    Objective get(
        Scoreboard scoreboard,
        String name,
        ChatComponent displayName,
        Criteria criteria,
        RenderType type);
  }
}