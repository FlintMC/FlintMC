package net.flintmc.util.session.v1_16_4;

import net.flintmc.transform.shadow.FieldSetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.util.Session;

// Minecraft doesn't allow for changing the session
@Shadow(value = "net.minecraft.client.Minecraft", version = "1.16.4")
public interface SessionRefreshableMinecraft {

  @FieldSetter("session")
  void setSession(Session session);
}
