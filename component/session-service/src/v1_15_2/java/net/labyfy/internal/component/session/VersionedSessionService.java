package net.labyfy.internal.component.session;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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

@Singleton
@Implement(value = SessionService.class, version = "1.15.2")
public class VersionedSessionService extends DefaultSessionService {

  @Inject
  public VersionedSessionService(@InjectLogger Logger logger, RefreshTokenResult.Factory refreshTokenResultFactory,
                                 GameProfileSerializer profileSerializer, SessionAccountLogInEvent.Factory logInEventFactory,
                                 SessionTokenRefreshEvent.Factory tokenRefreshEventFactory, AuthenticationResult.Factory authResultFactory) {
    super(logger, refreshTokenResultFactory, profileSerializer, logInEventFactory, tokenRefreshEventFactory, authResultFactory, Minecraft.getInstance().getProxy());
  }

  @Override
  protected void refreshSession() {
    SessionRefreshableMinecraft minecraft = (SessionRefreshableMinecraft) Minecraft.getInstance();
    minecraft.setSession(new net.minecraft.util.Session(
        super.getUsername(),
        super.getUniqueId().toString().replace("-", ""),
        super.getAccessToken(),
        net.minecraft.util.Session.Type.MOJANG.toString()
    ));
  }
}
