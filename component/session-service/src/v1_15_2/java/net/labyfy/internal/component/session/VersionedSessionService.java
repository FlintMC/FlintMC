package net.labyfy.internal.component.session;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.session.AuthenticationResult;
import net.labyfy.component.session.RefreshTokenResult;
import net.labyfy.component.session.SessionService;
import net.labyfy.component.session.event.SessionAccountLogInEvent;
import net.labyfy.component.session.event.SessionTokenRefreshEvent;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

import java.net.Proxy;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Singleton
@Implement(value = SessionService.class, version = "1.15.2")
public class VersionedSessionService extends DefaultSessionService {

  private static final Consumer<SessionService> REFRESHER = service -> {
    String uuid = (service.getUniqueId() != null ? service.getUniqueId() : UUID.randomUUID()).toString();
    String name = service.getUsername() != null ? service.getUsername() : uuid.split("-")[0];
    String accessToken = service.getAccessToken() != null ? service.getAccessToken() : "0";

    SessionRefreshableMinecraft minecraft = (SessionRefreshableMinecraft) Minecraft.getInstance();
    minecraft.setSession(new net.minecraft.util.Session(name, uuid, accessToken,
        net.minecraft.util.Session.Type.MOJANG.toString()));
  };
  private static final Supplier<Proxy> PROXY_SUPPLIER = () -> Minecraft.getInstance().getProxy();

  @Inject
  private VersionedSessionService(@InjectLogger Logger logger, RefreshTokenResult.Factory refreshTokenResultFactory,
                                  GameProfileSerializer profileSerializer, SessionAccountLogInEvent.Factory logInEventFactory,
                                  SessionTokenRefreshEvent.Factory tokenRefreshEventFactory, AuthenticationResult.Factory authResultFactory,
                                  EventBus eventBus) {
    super(logger, refreshTokenResultFactory, profileSerializer, logInEventFactory, tokenRefreshEventFactory,
        authResultFactory, eventBus, PROXY_SUPPLIER, REFRESHER);
  }

  private VersionedSessionService(@InjectLogger Logger logger, RefreshTokenResult.Factory refreshTokenResultFactory,
                                  GameProfileSerializer profileSerializer, AuthenticationResult.Factory authResultFactory,
                                  EventBus eventBus) {
    super(logger, refreshTokenResultFactory, profileSerializer, null, null,
        authResultFactory, eventBus, PROXY_SUPPLIER, null);
  }

  @Override
  public SessionService newSessionService() {
    return new VersionedSessionService(super.logger, super.refreshTokenResultFactory, super.profileSerializer, super.authResultFactory, super.eventBus);
  }
}
