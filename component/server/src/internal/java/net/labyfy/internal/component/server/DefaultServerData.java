package net.labyfy.internal.component.server;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.server.ServerAddress;
import net.labyfy.component.server.ServerData;
import net.labyfy.component.server.status.ServerStatus;
import net.labyfy.component.server.status.ServerStatusResolver;

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;

/** {@inheritDoc} */
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

  /** {@inheritDoc} */
  @Override
  public CompletableFuture<ServerStatus> loadStatus() throws UnknownHostException {
    return this.serverStatusResolver.resolveStatus(this.address);
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return this.name;
  }

  /** {@inheritDoc} */
  @Override
  public ServerAddress getServerAddress() {
    return this.address;
  }

  /** {@inheritDoc} */
  @Override
  public ResourceMode getResourceMode() {
    return this.resourceMode;
  }
}
