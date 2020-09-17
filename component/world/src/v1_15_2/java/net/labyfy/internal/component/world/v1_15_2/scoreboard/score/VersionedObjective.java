package net.labyfy.internal.component.world.v1_15_2.scoreboard.score;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.scoreboad.score.Criteria;
import net.labyfy.component.world.scoreboad.score.Objective;
import net.labyfy.component.world.scoreboad.type.RenderType;

/**
 *
 */
@Implement(value = Objective.class, version = "1.15.2")
public class VersionedObjective implements Objective {

  private final String name;
  private final ChatComponent displayName;
  private final Criteria criteria;
  private final RenderType renderType;

  @AssistedInject
  private VersionedObjective(
          @Assisted("name") String name,
          @Assisted("displayName") ChatComponent displayName,
          @Assisted("criteria") Criteria criteria,
          @Assisted("renderType") RenderType renderType
  ) {
    this.name = name;
    this.displayName = displayName;
    this.criteria = criteria;
    this.renderType = renderType;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  @Override
  public Criteria getCriteria() {
    return this.criteria;
  }

  @Override
  public RenderType getRenderType() {
    return this.renderType;
  }
}
