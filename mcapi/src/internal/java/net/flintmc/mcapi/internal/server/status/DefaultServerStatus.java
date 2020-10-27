package net.flintmc.mcapi.internal.server.status;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.status.ServerFavicon;
import net.flintmc.mcapi.server.status.ServerPlayers;
import net.flintmc.mcapi.server.status.ServerStatus;
import net.flintmc.mcapi.server.status.ServerVersion;

@Implement(ServerStatus.class)
public class DefaultServerStatus implements ServerStatus {

  private final ServerAddress sourceAddress;
  private final ServerVersion version;
  private final ServerPlayers players;
  private final ChatComponent description;
  private final ServerFavicon favicon;
  private final long ping;
  private final long timestamp;

  @AssistedInject
  public DefaultServerStatus(
      @Assisted("sourceAddress") ServerAddress sourceAddress,
      @Assisted("version") ServerVersion version,
      @Assisted("players") ServerPlayers players,
      @Assisted("description") ChatComponent description,
      @Assisted("favicon") ServerFavicon favicon,
      @Assisted("ping") long ping) {
    this.sourceAddress = sourceAddress;
    this.version = version;
    this.players = players;
    this.description = description;
    this.favicon = favicon;
    this.ping = ping;
    this.timestamp = System.currentTimeMillis();
  }

  /** {@inheritDoc} */
  @Override
  public ServerAddress getSourceAddress() {
    return this.sourceAddress;
  }

  /** {@inheritDoc} */
  @Override
  public ServerVersion getVersion() {
    return this.version;
  }

  /** {@inheritDoc} */
  @Override
  public ServerPlayers getPlayers() {
    return this.players;
  }

  /** {@inheritDoc} */
  @Override
  public ChatComponent getDescription() {
    return this.description;
  }

  /** {@inheritDoc} */
  @Override
  public ServerFavicon getFavicon() {
    return this.favicon;
  }

  /** {@inheritDoc} */
  @Override
  public long getPing() {
    return this.ping;
  }

  /** {@inheritDoc} */
  @Override
  public long getTimestamp() {
    return this.timestamp;
  }
}
