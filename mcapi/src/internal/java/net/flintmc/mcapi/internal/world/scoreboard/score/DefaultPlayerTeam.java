package net.flintmc.mcapi.internal.world.scoreboard.score;

import com.beust.jcommander.internal.Sets;
import java.util.Collection;
import java.util.Set;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.builder.TextComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.world.scoreboad.score.PlayerTeam;
import net.flintmc.mcapi.world.scoreboad.type.CollisionType;
import net.flintmc.mcapi.world.scoreboad.type.VisibleType;

@Implement(PlayerTeam.class)
public class DefaultPlayerTeam implements PlayerTeam {

  private final String name;
  private final Set<String> members;
  private ChatComponent displayName;
  private ChatComponent prefix;
  private ChatComponent suffix;
  private boolean allowFriendlyFire;
  private boolean canSeeFriendlyInvisible;
  private VisibleType nameTagVisibility;
  private VisibleType deathMessageVisibility;
  private ChatColor chatColor;
  private CollisionType collisionType;

  @AssistedInject
  public DefaultPlayerTeam(
      TextComponentBuilder textComponentBuilder, @Assisted("name") String name) {
    this.members = Sets.newHashSet();
    this.name = name;
    this.displayName = textComponentBuilder.text(name).build();
    this.prefix = textComponentBuilder.text("").build();
    this.suffix = textComponentBuilder.text("").build();

    this.allowFriendlyFire = true;
    this.canSeeFriendlyInvisible = true;
    this.nameTagVisibility = VisibleType.ALWAYS;
    this.deathMessageVisibility = VisibleType.ALWAYS;
    this.chatColor = ChatColor.WHITE;
    this.collisionType = CollisionType.ALWAYS;
  }

  /** {@inheritDoc} */
  @Override
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  /** {@inheritDoc} */
  @Override
  public void setDisplayName(ChatComponent displayName) {
    this.displayName = displayName;
  }

  /** {@inheritDoc} */
  @Override
  public ChatComponent getPrefix() {
    return this.prefix;
  }

  /** {@inheritDoc} */
  @Override
  public void setPrefix(ChatComponent prefix) {
    this.prefix = prefix;
  }

  /** {@inheritDoc} */
  @Override
  public ChatComponent getSuffix() {
    return this.suffix;
  }

  /** {@inheritDoc} */
  @Override
  public void setSuffix(ChatComponent suffix) {
    this.suffix = suffix;
  }

  /** {@inheritDoc} */
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

  @Override
  public void setFriendlyFlags(int flags) {
    this.setAllowFriendlyFire((flags & 1) > 0);
    this.setSeeFriendlyInvisible((flags & 2) > 0);
  }

  /** {@inheritDoc} */
  @Override
  public void setColor(ChatColor color) {
    this.chatColor = color;
  }

  /** {@inheritDoc} */
  @Override
  public void setAllowFriendlyFire(boolean friendlyFire) {
    this.allowFriendlyFire = friendlyFire;
  }

  /** {@inheritDoc} */
  @Override
  public void setSeeFriendlyInvisible(boolean friendlyInvisible) {
    this.canSeeFriendlyInvisible = friendlyInvisible;
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return this.name;
  }

  /** {@inheritDoc} */
  @Override
  public Collection<String> getMembers() {
    return this.members;
  }

  /** {@inheritDoc} */
  @Override
  public ChatColor getTeamColor() {
    return this.chatColor;
  }

  /** {@inheritDoc} */
  @Override
  public boolean seeFriendlyInvisible() {
    return this.canSeeFriendlyInvisible;
  }

  /** {@inheritDoc} */
  @Override
  public boolean allowFriendlyFire() {
    return this.allowFriendlyFire;
  }

  /** {@inheritDoc} */
  @Override
  public VisibleType getNameTagVisibility() {
    return this.nameTagVisibility;
  }

  /** {@inheritDoc} */
  @Override
  public void setNameTagVisibility(VisibleType visibility) {
    this.nameTagVisibility = visibility;
  }

  /** {@inheritDoc} */
  @Override
  public VisibleType getDeathMessageVisibility() {
    return this.deathMessageVisibility;
  }

  /** {@inheritDoc} */
  @Override
  public void setDeathMessageVisibility(VisibleType visibility) {
    this.deathMessageVisibility = visibility;
  }

  /** {@inheritDoc} */
  @Override
  public CollisionType getCollisionType() {
    return this.collisionType;
  }

  /** {@inheritDoc} */
  @Override
  public void setCollisionType(CollisionType type) {
    this.collisionType = type;
  }
}
