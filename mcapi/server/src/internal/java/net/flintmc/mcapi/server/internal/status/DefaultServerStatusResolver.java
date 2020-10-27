package net.flintmc.mcapi.server.internal.status;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.status.ServerFavicon;
import net.flintmc.mcapi.server.status.ServerStatus;
import net.flintmc.mcapi.server.status.ServerStatusResolver;
import net.flintmc.mcapi.server.status.pending.PendingStatusRequest;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Implement(value = ServerStatusResolver.class, version = "1.15.2")
public class DefaultServerStatusResolver implements ServerStatusResolver {

  private final Map<ServerAddress, PendingStatusRequest> pendingRequests = new ConcurrentHashMap<>();
  private final PendingStatusRequest.Factory statusRequestFactory;
  private final ServerFavicon defaultFavicon;

  @Inject
  public DefaultServerStatusResolver(ResourceLocationProvider resourceLocationProvider,
                                     PendingStatusRequest.Factory statusRequestFactory,
                                     ServerFavicon.Factory faviconFactory) {
    this.statusRequestFactory = statusRequestFactory;
    this.defaultFavicon = faviconFactory.createDefault(resourceLocationProvider.get("textures/misc/unknown_server.png"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<ServerStatus> resolveStatus(ServerAddress address) throws UnknownHostException {
    if (this.pendingRequests.containsKey(address)) {
      PendingStatusRequest request = this.pendingRequests.get(address);
      if (request != null) {
        return request.getFuture();
      }
    }

    PendingStatusRequest request = this.statusRequestFactory.create(address, this.defaultFavicon);

    request.getFuture().thenAccept(status -> this.pendingRequests.remove(address));
    this.pendingRequests.put(address, request);

    request.start();

    return request.getFuture();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<PendingStatusRequest> getPendingRequests() {
    return Collections.unmodifiableCollection(this.pendingRequests.values());
  }
}
