package net.labyfy.component.world.scoreboad.team;

import net.labyfy.chat.format.ChatColor;
import net.labyfy.component.world.scoreboad.type.CollisionType;
import net.labyfy.component.world.scoreboad.type.VisibleType;

import java.util.Collection;

/**
 * Represents a Minecraft team.
 */
public interface Team {

  /**
   * Whether the given team is the same team of this object.
   *
   * @param other The team to be checked.
   * @return {@code true} if the given team is the same, otherwise {@code false}.
   */
  default boolean isSameTeam(Team other) {
    return this == other;
  }

  /**
   * Retrieves the registry name of this team.
   *
   * @return The registry name.
   */
  String getName();

  /**
   * Retrieves a collection of all members in this team.
   *
   * @return a collection of all members.
   */
  Collection<String> getMembers();

  /**
   * Retrieves the color of this team.
   *
   * @return The team color.
   */
  ChatColor getTeamColor();

  /**
   * Whether friendly invisible members may be seen.
   *
   * @return {@code true} if friendly invisible members can be seen, otherwise {@code false}.
   */
  boolean seeFriendlyInvisible();

  /**
   * Whether friendly fire is allowed in this team.
   *
   * @return {@code true} if friendly fire allowed, otherwise {@code false}.
   */
  boolean allowFriendlyFire();

  /**
   * Retrieves the name tag visibility of this team.
   *
   * @return The name tag visibility.
   */
  VisibleType getNameTagVisibility();

  /**
   * Retrieves the death message visibility of this team.
   *
   * @return The death message visibility.
   */
  VisibleType getDeathMessageVisibility();

  /**
   * Retrieves the collision rule of this team.
   *
   * @return The collision rule.
   */
  CollisionType getCollisionType();

}
