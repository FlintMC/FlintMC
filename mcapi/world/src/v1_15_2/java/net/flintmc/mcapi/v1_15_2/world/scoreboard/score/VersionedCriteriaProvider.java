package net.flintmc.mcapi.v1_15_2.world.scoreboard.score;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.scoreboad.score.Criteria;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

/**
 * 1.15.2 implementation of {@link Criteria.Provider}
 */
@Singleton
@Implement(value = Criteria.Provider.class, version = "1.15.2")
public class VersionedCriteriaProvider implements Criteria.Provider {

  private final VersionedCriteria.Factory criteriaFactory;

  @Inject
  public VersionedCriteriaProvider(Criteria.Factory criteriaFactory) {
    this.criteriaFactory = criteriaFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Criteria get(String name) {
    return this.criteriaFactory.create(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Criteria get(String name, boolean readOnly, RenderType renderType) {
    return this.criteriaFactory.create(name, readOnly, renderType);
  }

}
