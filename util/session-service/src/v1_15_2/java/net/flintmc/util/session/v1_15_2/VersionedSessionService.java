/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.util.session.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.util.UUIDTypeAdapter;
import java.io.IOException;
import java.net.Proxy;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.mcapi.event.MinecraftInitializeEvent;
import net.flintmc.mcapi.player.serializer.gameprofile.GameProfileSerializer;
import net.flintmc.util.session.AuthenticationResult;
import net.flintmc.util.session.RefreshTokenResult;
import net.flintmc.util.session.SessionService;
import net.flintmc.util.session.event.SessionAccountLogInEvent;
import net.flintmc.util.session.event.SessionTokenRefreshEvent;
import net.flintmc.util.session.internal.DefaultSessionService;
import net.flintmc.util.session.launcher.LauncherProfileResolver;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.apache.logging.log4j.Logger;

@Singleton
@Implement(value = SessionService.class, version = "1.15.2")
public class VersionedSessionService extends DefaultSessionService {

  private static final Consumer<SessionService> REFRESHER =
      service -> {
        if (service.getUniqueId() == null) {
          return;
        }

        String uuid = service.getUniqueId().toString().replace("-", "");
        String name = service.getUsername();
        String accessToken = service.getAccessToken();

        SessionRefreshableMinecraft minecraft =
            (SessionRefreshableMinecraft) Minecraft.getInstance();
        minecraft.setSession(
            new net.minecraft.util.Session(
                name, uuid, accessToken, net.minecraft.util.Session.Type.MOJANG.toString()));
      };
  private static final Supplier<Proxy> PROXY_SUPPLIER = () -> Minecraft.getInstance().getProxy();

  private final LauncherProfileResolver resolver;

  @Inject
  private VersionedSessionService(
      @InjectLogger Logger logger,
      RefreshTokenResult.Factory refreshTokenResultFactory,
      GameProfileSerializer profileSerializer,
      SessionAccountLogInEvent.Factory logInEventFactory,
      SessionTokenRefreshEvent.Factory tokenRefreshEventFactory,
      AuthenticationResult.Factory authResultFactory,
      EventBus eventBus,
      LauncherProfileResolver resolver) {
    super(
        logger,
        refreshTokenResultFactory,
        profileSerializer,
        logInEventFactory,
        tokenRefreshEventFactory,
        authResultFactory,
        eventBus,
        PROXY_SUPPLIER,
        REFRESHER);
    this.resolver = resolver;
  }

  private VersionedSessionService(
      @InjectLogger Logger logger,
      RefreshTokenResult.Factory refreshTokenResultFactory,
      GameProfileSerializer profileSerializer,
      AuthenticationResult.Factory authResultFactory,
      EventBus eventBus,
      LauncherProfileResolver resolver) {
    super(
        logger,
        refreshTokenResultFactory,
        profileSerializer,
        null,
        null,
        authResultFactory,
        eventBus,
        PROXY_SUPPLIER,
        null);
    this.resolver = resolver;
  }

  @Override
  public SessionService newSessionService() {
    return new VersionedSessionService(
        super.logger,
        super.refreshTokenResultFactory,
        super.profileSerializer,
        super.authResultFactory,
        super.eventBus,
        this.resolver);
  }

  @Subscribe(phase = Subscribe.Phase.POST, version = "1.15.2")
  public void initSession(MinecraftInitializeEvent event) throws IOException {
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
        session.getToken());
  }
}
