package net.labyfy.internal.component.server.status;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.server.status.ServerPlayers;

@Implement(ServerPlayers.class)
public class DefaultServerPlayers implements ServerPlayers {

  private final int online;
  private final int max;
  private final GameProfile[] players;

  @AssistedInject
  public DefaultServerPlayers(@Assisted("online") int online, @Assisted("max") int max, @Assisted("players") GameProfile[] players) {
    this.online = online;
    this.max = max;
    this.players = players;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getOnlinePlayerCount() {
    return this.online;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxPlayerCount() {
    return this.max;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameProfile[] getOnlinePlayers() {
    return this.players;
  }
}
