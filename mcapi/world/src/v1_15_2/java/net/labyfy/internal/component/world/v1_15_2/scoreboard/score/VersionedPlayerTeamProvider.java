package net.labyfy.internal.component.world.v1_15_2.scoreboard.score;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.builder.TextComponentBuilder;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.scoreboad.score.PlayerTeam;

/**
 * 1.15.2 implementation of {@link PlayerTeam.Provider}
 */
@Singleton
@Implement(value = PlayerTeam.Provider.class, version = "1.15.2")
public class VersionedPlayerTeamProvider implements PlayerTeam.Provider {

  private final PlayerTeam.Factory playerTeamFactory;
  private final Scoreboard scoreboard;
  private final TextComponentBuilder textComponentBuilder;

  @Inject
  public VersionedPlayerTeamProvider(
          PlayerTeam.Factory playerTeamFactory,
          Scoreboard scoreboard,
          TextComponentBuilder textComponentBuilder
  ) {
    this.playerTeamFactory = playerTeamFactory;
    this.scoreboard = scoreboard;
    this.textComponentBuilder = textComponentBuilder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PlayerTeam get(String name) {
    return this.playerTeamFactory.create(scoreboard, name, this.textComponentBuilder.text(name).build());
  }
}
