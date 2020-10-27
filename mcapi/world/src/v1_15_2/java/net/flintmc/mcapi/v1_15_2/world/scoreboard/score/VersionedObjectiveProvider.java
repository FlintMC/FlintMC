package net.flintmc.mcapi.v1_15_2.world.scoreboard.score;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.scoreboad.score.Criteria;
import net.flintmc.mcapi.world.scoreboad.score.Objective;
import net.flintmc.mcapi.world.scoreboad.type.RenderType;

/**
 * 1.15.2 implementation of {@link Objective.Provider}
 */
@Singleton
@Implement(value = Objective.Provider.class, version = "1.15.2")
public class VersionedObjectiveProvider implements Objective.Provider {

  private final Objective.Factory objectiveFactory;

  @Inject
  public VersionedObjectiveProvider(Objective.Factory objectiveFactory) {
    this.objectiveFactory = objectiveFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Objective get(
          Scoreboard scoreboard,
          String name,
          ChatComponent displayName,
          Criteria criteria,
          RenderType type
  ) {
    return this.objectiveFactory.create(scoreboard, name, displayName, criteria, type);
  }

}
