package net.flintmc.mcapi.v1_15_2.server.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.event.ServerLoginSuccessEvent;
import net.flintmc.transform.hook.Hook;
import net.minecraft.client.network.login.ClientLoginNetHandler;
import net.minecraft.network.login.server.SLoginSuccessPacket;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Singleton
public class ServerLoginSuccessEventInjector {

  private final EventBus eventBus;
  private final ServerAddress.Factory addressFactory;
  private final ServerLoginSuccessEvent.Factory eventFactory;

  @Inject
  public ServerLoginSuccessEventInjector(
      EventBus eventBus,
      ServerAddress.Factory addressFactory,
      ServerLoginSuccessEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.addressFactory = addressFactory;
    this.eventFactory = eventFactory;
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      className = "net.minecraft.client.network.login.ClientLoginNetHandler",
      methodName = "handleLoginSuccess",
      parameters = @Type(reference = SLoginSuccessPacket.class))
  public void handleLoginSuccess(
      @Named("instance") Object instance, Hook.ExecutionTime executionTime) {
    ClientLoginNetHandler handler = (ClientLoginNetHandler) instance;
    SocketAddress socketAddress = handler.getNetworkManager().getRemoteAddress();

    ServerAddress address =
        socketAddress instanceof InetSocketAddress
            ? this.addressFactory.create((InetSocketAddress) socketAddress)
            : null;
    this.eventBus.fireEvent(this.eventFactory.create(address), executionTime);
  }
}
