package net.flintmc.mcapi.internal.world.scoreboard.listener;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.world.scoreboad.score.PlayerTeam;
import net.flintmc.mcapi.world.scoreboad.type.CollisionType;
import net.flintmc.mcapi.world.scoreboad.type.VisibleType;

public interface PlayerTeamChangeListener {

  void changeDisplayName(PlayerTeam playerTeam, ChatComponent displayName);

  void changePrefix(PlayerTeam playerTeam, ChatComponent prefix);

  void changeSuffix(PlayerTeam playerTeam, ChatComponent suffix);

  void changeColor(PlayerTeam playerTeam, ChatColor chatColor);

  void changeAllowFriendlyFire(PlayerTeam playerTeam, boolean friendlyFire);

  void changeSeeFriendlyInvisible(PlayerTeam playerTeam, boolean friendlyInvisible);

  void changeNameTagVisibility(PlayerTeam playerTeam, VisibleType visibleType);

  void changeDeathMessageVisibility(PlayerTeam playerTeam, VisibleType visibleType);

  void changeCollisionType(PlayerTeam playerTeam, CollisionType collisionType);
}
