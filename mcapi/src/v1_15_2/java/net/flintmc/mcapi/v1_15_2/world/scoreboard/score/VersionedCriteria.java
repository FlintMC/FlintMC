package net.flintmc.mcapi.v1_15_2.world.scoreboard.score;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.scoreboad.score.Criteria;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

/** 1.15.2 implementation of {@link Criteria} */
@Implement(value = Criteria.class, version = "1.15.2")
public class VersionedCriteria implements Criteria {

  private final String name;
  private final boolean readOnly;
  private final RenderType renderType;

  @AssistedInject
  private VersionedCriteria(@Assisted("name") String name) {
    this(name, false, RenderType.INTEGER);
  }

  @AssistedInject
  private VersionedCriteria(
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
