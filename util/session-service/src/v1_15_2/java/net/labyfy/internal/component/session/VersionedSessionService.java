package net.labyfy.internal.component.session;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.util.UUIDTypeAdapter;
import net.flintmc.framework.eventbus.EventBus;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.session.AuthenticationResult;
import net.labyfy.component.session.RefreshTokenResult;
import net.labyfy.component.session.SessionService;
import net.labyfy.component.session.event.SessionAccountLogInEvent;
import net.labyfy.component.session.event.SessionTokenRefreshEvent;
import net.labyfy.component.session.launcher.LauncherProfileResolver;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Proxy;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Singleton
@Implement(value = SessionService.class, version = "1.15.2")
public class VersionedSessionService extends DefaultSessionService {

  private static final Consumer<SessionService> REFRESHER = service -> {
    if (service.getUniqueId() == null) {
      return;
    }

    String uuid = service.getUniqueId().toString().replace("-", "");
    String name = service.getUsername();
    String accessToken = service.getAccessToken();

    SessionRefreshableMinecraft minecraft = (SessionRefreshableMinecraft) Minecraft.getInstance();
    minecraft.setSession(new net.minecraft.util.Session(name, uuid, accessToken,
        net.minecraft.util.Session.Type.MOJANG.toString()));
  };
  private static final Supplier<Proxy> PROXY_SUPPLIER = () -> Minecraft.getInstance().getProxy();

  private final LauncherProfileResolver resolver;

  @Inject
  private VersionedSessionService(@InjectLogger Logger logger, RefreshTokenResult.Factory refreshTokenResultFactory,
                                  GameProfileSerializer profileSerializer, SessionAccountLogInEvent.Factory logInEventFactory,
                                  SessionTokenRefreshEvent.Factory tokenRefreshEventFactory, AuthenticationResult.Factory authResultFactory,
                                  EventBus eventBus, LauncherProfileResolver resolver) {
    super(logger, refreshTokenResultFactory, profileSerializer, logInEventFactory, tokenRefreshEventFactory,
        authResultFactory, eventBus, PROXY_SUPPLIER, REFRESHER);
    this.resolver = resolver;
  }

  private VersionedSessionService(@InjectLogger Logger logger, RefreshTokenResult.Factory refreshTokenResultFactory,
                                  GameProfileSerializer profileSerializer, AuthenticationResult.Factory authResultFactory,
                                  EventBus eventBus, LauncherProfileResolver resolver) {
    super(logger, refreshTokenResultFactory, profileSerializer, null, null,
        authResultFactory, eventBus, PROXY_SUPPLIER, null);
    this.resolver = resolver;
  }

  @Override
  public SessionService newSessionService() {
    return new VersionedSessionService(super.logger, super.refreshTokenResultFactory, super.profileSerializer, super.authResultFactory, super.eventBus, this.resolver);
  }

  @Task(Tasks.POST_MINECRAFT_INITIALIZE)
  public void initSession() throws IOException {
    // load the session that has been given to the client by the launcher
    Session session = Minecraft.getInstance().getSession();
    if (session.getToken().equals("0")) {
      // not authenticated by the launcher
      return;
    }

    super.ensureAuthenticationAvailable();
    super.updateAuthenticationContent(
        UUIDTypeAdapter.fromString(session.getPlayerID()),
        session.getUsername(),
        session.getToken()
    );
  }

}
