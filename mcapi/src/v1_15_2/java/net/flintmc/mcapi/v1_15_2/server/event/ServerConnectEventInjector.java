package net.flintmc.mcapi.v1_15_2.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.event.ServerConnectEvent;
import net.flintmc.transform.hook.Hook;

@Singleton
public class ServerConnectEventInjector {

  private final EventBus eventBus;
  private final ServerAddress.Factory addressFactory;
  private final ServerConnectEvent.Factory eventFactory;

  @Inject
  public ServerConnectEventInjector(
      EventBus eventBus,
      ServerAddress.Factory addressFactory,
      ServerConnectEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.addressFactory = addressFactory;
    this.eventFactory = eventFactory;
  }

  @Hook(
      executionTime = Hook.ExecutionTime.BEFORE,
      className = "net.minecraft.client.gui.screen.ConnectingScreen",
      methodName = "connect",
      parameters = {@Type(reference = String.class), @Type(reference = int.class)})
  public void handleConnect(Hook.ExecutionTime executionTime, @Named("args") Object[] args) {
    ServerAddress address = this.addressFactory.create((String) args[0], (int) args[1]);
    this.eventBus.fireEvent(this.eventFactory.create(address), executionTime);
  }
}
