package net.flintmc.mcapi.internal.world.scoreboard.score;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.scoreboad.score.Criteria;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

@Implement(Criteria.class)
public class DefaultCriteria implements Criteria {

  private final String name;
  private final boolean readOnly;
  private final RenderType renderType;

  @AssistedInject
  private DefaultCriteria(@Assisted("name") String name) {
    this(name, false, RenderType.INTEGER);
  }

  @AssistedInject
  private DefaultCriteria(
      @Assisted("name") String name,
      @Assisted("readOnly") boolean readOnly,
      @Assisted("renderType") RenderType renderType) {
    this.name = name;
    this.readOnly = readOnly;
    this.renderType = renderType;
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return this.name;
  }

  /** {@inheritDoc} */
  @Override
  public boolean readOnly() {
    return this.readOnly;
  }

  /** {@inheritDoc} */
  @Override
  public RenderType getRenderType() {
    return this.renderType;
  }
}
