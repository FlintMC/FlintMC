package net.flintmc.mcapi.internal.server.status;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.server.status.ServerPlayers;

@Implement(ServerPlayers.class)
public class DefaultServerPlayers implements ServerPlayers {

  private final int online;
  private final int max;
  private final GameProfile[] players;

  @AssistedInject
  public DefaultServerPlayers(
      @Assisted("online") int online,
      @Assisted("max") int max,
      @Assisted("players") GameProfile[] players) {
    this.online = online;
    this.max = max;
    this.players = players;
  }

  /** {@inheritDoc} */
  @Override
  public int getOnlinePlayerCount() {
    return this.online;
  }

  /** {@inheritDoc} */
  @Override
  public int getMaxPlayerCount() {
    return this.max;
  }

  /** {@inheritDoc} */
  @Override
  public GameProfile[] getOnlinePlayers() {
    return this.players;
  }
}
