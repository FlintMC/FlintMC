package net.labyfy.internal.component.session;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.session.Session;
import net.labyfy.component.session.refresh.RefreshTokenResult;
import net.minecraft.client.Minecraft;

@Singleton
@Implement(value = Session.class, version = "1.15.2")
public class VersionedSession extends DefaultSession {

  @Inject
  public VersionedSession(RefreshTokenResult.Factory refreshTokenResultFactory, GameProfileSerializer profileSerializer) {
    super(refreshTokenResultFactory, profileSerializer, Minecraft.getInstance().getProxy());
  }

  @Override
  protected void refreshSession() {
    SessionRefreshableMinecraft minecraft = (SessionRefreshableMinecraft) Minecraft.getInstance();
    minecraft.setSession(new net.minecraft.util.Session(
        super.getUsername(),
        super.getUniqueId().toString(),
        super.getAccessToken(),
        net.minecraft.util.Session.Type.MOJANG.toString()
    ));
  }
}
