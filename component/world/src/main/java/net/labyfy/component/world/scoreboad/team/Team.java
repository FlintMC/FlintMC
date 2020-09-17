package net.labyfy.component.world.scoreboad.team;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.format.ChatColor;
import net.labyfy.component.world.scoreboad.type.CollisionType;
import net.labyfy.component.world.scoreboad.type.VisibleType;

import java.util.Collection;

/**
 *
 */
public interface Team {

  default boolean isSameTeam(Team other) {
    return this == other;
  }

  String getName();

  ChatComponent format(ChatComponent component);

  Collection<String> getMembers();

  ChatColor getTeamColor();

  boolean seeFriendlyInvisible();

  boolean allowFriendlyFire();

  VisibleType getNameTagVisibility();

  VisibleType getDeathMessageVisibility();

  CollisionType getCollisionType();

}
