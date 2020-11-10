package net.flintmc.mcapi.internal.server;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerData;
import net.flintmc.mcapi.server.status.ServerStatus;
import net.flintmc.mcapi.server.status.ServerStatusResolver;

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;

/**
 * {@inheritDoc}
 */
@Implement(ServerData.class)
public class DefaultServerData implements ServerData {

  private final String name;
  private final ServerAddress address;
  private final ResourceMode resourceMode;

  private final ServerStatusResolver serverStatusResolver;

  @AssistedInject
  private DefaultServerData(
          @Assisted String name,
          @Assisted ServerAddress address,
          @Assisted ResourceMode resourceMode,
          ServerStatusResolver serverStatusResolver) {
    this.name = name;
    this.address = address;
    this.resourceMode = resourceMode;
    this.serverStatusResolver = serverStatusResolver;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<ServerStatus> loadStatus() throws UnknownHostException {
    return this.serverStatusResolver.resolveStatus(this.address);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ServerAddress getServerAddress() {
    return this.address;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceMode getResourceMode() {
    return this.resourceMode;
  }
}
