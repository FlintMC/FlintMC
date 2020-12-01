package net.flintmc.mcapi.v1_15_2.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerController;
import net.flintmc.mcapi.server.event.ServerDisconnectEvent;
import net.flintmc.transform.hook.Hook;

@Singleton
public class ServerDisconnectEventInjector {

  private final EventBus eventBus;
  private final ServerController controller;
  private final ServerDisconnectEvent.Factory eventFactory;

  @Inject
  public ServerDisconnectEventInjector(
      EventBus eventBus, ServerController controller, ServerDisconnectEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.controller = controller;
    this.eventFactory = eventFactory;
  }

  @Hook(
      className = "net.minecraft.client.world.ClientWorld",
      methodName = "sendQuittingDisconnectingPacket")
  public void handleDisconnect(Hook.ExecutionTime executionTime) {
    ServerAddress address =
        this.controller.isConnected() ? this.controller.getConnectedServer().getAddress() : null;
    this.eventBus.fireEvent(this.eventFactory.create(address), executionTime);
  }
}
