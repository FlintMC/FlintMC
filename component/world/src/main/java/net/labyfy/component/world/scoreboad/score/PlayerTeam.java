package net.labyfy.component.world.scoreboad.score;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.format.ChatColor;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.scoreboad.team.Team;
import net.labyfy.component.world.scoreboad.type.CollisionType;
import net.labyfy.component.world.scoreboad.type.VisibleType;

/**
 * Represents a score player team from Minecraft
 */
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
   * Sets the color of this team.
   *
   * @param color The new color for this team
   */
  void setColor(ChatColor color);

  /**
   * Changes the allow friendly fire  of this team.
   *
   * @param friendlyFire {@code true} if friendly fire allowed, otherwise {@code false}
   */
  void setAllowFriendlyFire(boolean friendlyFire);

  /**
   * Changes the friendly invisible of this team.
   *
   * @param friendlyInvisible {@code true} if can see friendly invisible members, otherwise {@code false}
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

  @AssistedFactory(PlayerTeam.class)
  interface Factory {

    /*
    PlayerTeam create(
            Scoreboard scoreboard,
            @Assisted("name") String name,
            @Assisted("chatComponent") ChatComponent displayName
    );*/

  }

  interface Provider {

    PlayerTeam get(String name);

  }

}
