package net.labyfy.internal.component.world.v1_15_2.scoreboard.score;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.format.ChatColor;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.scoreboad.score.PlayerTeam;
import net.labyfy.component.world.scoreboad.type.CollisionType;
import net.labyfy.component.world.scoreboad.type.VisibleType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Robby
 */
@Implement(value = PlayerTeam.class, version = "1.15.2")
public class VersionedPlayerTeam implements PlayerTeam {

  private final Set<String> members = new HashSet<>();

  private final Scoreboard scoreboard;
  private final String name;

  private boolean allowFriendlyFire;
  private boolean canSeeFriendlyInvisible;

  private ChatComponent displayName;
  private ChatComponent prefix;
  private ChatComponent suffix;

  private VisibleType nameTagVisibility;
  private VisibleType deathMessageVisibility;
  private ChatColor color;
  private CollisionType collisionType;

  @AssistedInject
  private VersionedPlayerTeam(
          @Assisted("scoreboard") Scoreboard scoreboard,
          @Assisted("name") String name,
          @Assisted("chatComponent") ChatComponent displayName) {
    this.scoreboard = scoreboard;
    this.name = name;
    this.displayName = displayName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDisplayName(ChatComponent displayName) {
    this.displayName = displayName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getPrefix() {
    return this.prefix;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPrefix(ChatComponent prefix) {
    this.prefix = prefix;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getSuffix() {
    return this.suffix;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSuffix(ChatComponent suffix) {
    this.suffix = suffix;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFriendlyFlags() {
    int flag = 0;

    if (this.allowFriendlyFire()) {
      flag |= 1;
    }

    if (this.seeFriendlyInvisible()) {
      flag |= 2;
    }

    return flag;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setColor(ChatColor color) {
    this.color = color;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAllowFriendlyFire(boolean friendlyFire) {
    this.allowFriendlyFire = friendlyFire;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSeeFriendlyInvisible(boolean friendlyInvisible) {
    this.canSeeFriendlyInvisible = friendlyInvisible;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNameTagVisibility(VisibleType visibility) {
    this.nameTagVisibility = visibility;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDeathMessageVisibility(VisibleType visibility) {
    this.deathMessageVisibility = visibility;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCollisionType(CollisionType type) {
    this.collisionType = type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> getMembers() {
    return this.members;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatColor getTeamColor() {
    return this.color;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean seeFriendlyInvisible() {
    return this.canSeeFriendlyInvisible;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean allowFriendlyFire() {
    return this.allowFriendlyFire;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VisibleType getNameTagVisibility() {
    return this.nameTagVisibility;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VisibleType getDeathMessageVisibility() {
    return this.deathMessageVisibility;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CollisionType getCollisionType() {
    return this.collisionType;
  }

}
