package net.flintmc.mcapi.internal.player.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.event.PlayerInfoEvent;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;

@Implement(PlayerInfoEvent.class)
public class DefaultPlayerInfoEvent implements PlayerInfoEvent {

  private final Type type;
  private final NetworkPlayerInfo playerInfo;

  @AssistedInject
  public DefaultPlayerInfoEvent(@Assisted Type type, @Assisted NetworkPlayerInfo playerInfo) {
    this.type = type;
    this.playerInfo = playerInfo;
  }

  @Override
  public Type getType() {
    return this.type;
  }

  @Override
  public NetworkPlayerInfo getPlayerInfo() {
    return this.playerInfo;
  }
}
