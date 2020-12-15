package net.flintmc.mcapi.world.scoreboad.score;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.world.scoreboad.team.Team;
import net.flintmc.mcapi.world.scoreboad.type.CollisionType;
import net.flintmc.mcapi.world.scoreboad.type.VisibleType;

/** Represents a Minecraft score player team */
public interface PlayerTeam extends Team {

  /**
   * Retrieves the display name of this team
   *
   * @return The display name of this team
   */
  ChatComponent getDisplayName();

  /**
   * Changes the display name of this team.
   *
   * @param displayName The new display name
   */
  void setDisplayName(ChatComponent displayName);

  /**
   * Retrieves the prefix of this team
   *
   * @return The prefix of this team
   */
  ChatComponent getPrefix();

  /**
   * Sets a new prefix to this team
   *
   * @param prefix The new prefix
   */
  void setPrefix(ChatComponent prefix);

  /**
   * Retrieves the suffix of this team
   *
   * @return The suffix of this team
   */
  ChatComponent getSuffix();

  /**
   * Sets a new suffix to this team
   *
   * @param suffix The new suffix
   */
  void setSuffix(ChatComponent suffix);

  /**
   * Retrieves the friendly flags of this team
   *
   * @return The friendly flags of this team
   */
  int getFriendlyFlags();

  /**
   * Changes the friendly flags of this team.
   *
   * @param flags The new friendly flags.
   */
  void setFriendlyFlags(int flags);

  /**
   * Sets the color of this team.
   *
   * @param color The new color for this team
   */
  void setColor(ChatColor color);

  /**
   * Changes the allow friendly fire of this team.
   *
   * @param friendlyFire {@code true} if friendly fire allowed, otherwise {@code false}
   */
  void setAllowFriendlyFire(boolean friendlyFire);

  /**
   * Changes the friendly invisible of this team.
   *
   * @param friendlyInvisible {@code true} if can see friendly invisible members, otherwise {@code
   *     false}
   */
  void setSeeFriendlyInvisible(boolean friendlyInvisible);

  /**
   * Sets the name tag visibility of this team
   *
   * @param visibility The new name tag visibility
   */
  void setNameTagVisibility(VisibleType visibility);

  /**
   * Sets the death message visibility of this team.
   *
   * @param visibility The new death message visibility
   */
  void setDeathMessageVisibility(VisibleType visibility);

  /**
   * Sets the collision type of this team.
   *
   * @param type The new collision type
   */
  void setCollisionType(CollisionType type);

  /** A factory class for {@link PlayerTeam} */
  @AssistedFactory(PlayerTeam.class)
  interface Factory {

    /**
     * Creates a new {@link PlayerTeam} with the given parameters.
     *
     * @param name The registry name for this player team.
     * @return A created player team.
     */
    PlayerTeam create(@Assisted("name") String name);
  }
}
