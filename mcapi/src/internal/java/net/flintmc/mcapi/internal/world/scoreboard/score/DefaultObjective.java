package net.flintmc.mcapi.internal.world.scoreboard.score;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.scoreboad.score.Criteria;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

@Implement(Objective.class)
public class DefaultObjective implements Objective {

  private final Scoreboard scoreboard;
  private final String name;
  private final Criteria criteria;
  private ChatComponent displayName;
  private RenderType renderType;

  @AssistedInject
  public DefaultObjective(
      Scoreboard scoreboard,
      @Assisted("name") String name,
      @Assisted("displayName") ChatComponent displayName,
      @Assisted("criteria") Criteria criteria,
      @Assisted("renderType") RenderType renderType) {
    this.scoreboard = scoreboard;
    this.name = name;
    this.displayName = displayName;
    this.criteria = criteria;
    this.renderType = renderType;
  }

  /** {@inheritDoc} */
  @Override
  public Scoreboard getScoreboard() {
    return this.scoreboard;
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return this.name;
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
  public Criteria getCriteria() {
    return this.criteria;
  }

  /** {@inheritDoc} */
  @Override
  public RenderType getRenderType() {
    return this.renderType;
  }

  /** {@inheritDoc} */
  @Override
  public void setRenderType(RenderType renderType) {
    this.renderType = renderType;
  }
}
