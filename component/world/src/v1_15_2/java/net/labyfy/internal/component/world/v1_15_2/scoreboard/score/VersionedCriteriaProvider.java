package net.labyfy.internal.component.world.v1_15_2.scoreboard.score;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.world.scoreboad.score.Criteria;
import net.labyfy.component.world.scoreboad.type.RenderType;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.15.2 implementation of {@link Criteria.Provider}
 */
@AutoLoad
@Singleton
@Implement(Criteria.Provider.class)
public class VersionedCriteriaProvider implements Criteria.Provider {

  private final List<Criteria> criteriaRegistry;
  private final VersionedCriteria.Factory criteriaFactory;

  @Inject
  public VersionedCriteriaProvider(Criteria.Factory criteriaFactory) {
    this.criteriaRegistry = new ArrayList<>();
    this.criteriaFactory = criteriaFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Criteria get(String name) {
    Criteria criteria = this.getCriteria(name);

    if (criteria == null) {
      criteria = this.criteriaFactory.create(name);
      this.criteriaRegistry.add(criteria);
    }

    return criteria;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Criteria get(String name, boolean readOnly, RenderType renderType) {
    Criteria criteria = this.getCriteria(name);

    if (criteria == null) {
      criteria = this.criteriaFactory.create(name, readOnly, renderType);
      this.criteriaRegistry.add(criteria);
    }

    return criteria;
  }

  /**
   * Retrieves a registered criteria through the given name.
   *
   * @param name The name of the criteria.
   * @return A registered criteria with the name or {@code null}
   */
  private Criteria getCriteria(String name) {
    for (Criteria criteria : this.criteriaRegistry) {
      if (criteria.getName().equals(name)) return criteria;
    }
    return null;
  }

}
