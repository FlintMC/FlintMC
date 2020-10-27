package net.labyfy.internal.component.world.v1_15_2.scoreboard.score;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.scoreboad.score.Criteria;
import net.labyfy.component.world.scoreboad.type.RenderType;

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
