package net.flintmc.mcapi.internal.world.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.mcapi.world.event.WorldUnloadEvent;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;

@Singleton
public class DefaultScoreboardInvalidator {

  private final Scoreboard scoreboard;

  @Inject
  private DefaultScoreboardInvalidator(Scoreboard scoreboard) {
    this.scoreboard = scoreboard;
  }

  @PostSubscribe
  public void worldUnload(WorldUnloadEvent event) {
    this.scoreboard.invalidate();
  }

}
