package net.flintmc.util.session.v1_15_2;

import net.flintmc.transform.shadow.FieldSetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.util.Session;

// Minecraft doesn't allow for changing the session
@Shadow("net.minecraft.client.Minecraft")
public interface SessionRefreshableMinecraft {

  @FieldSetter(value = "session", removeFinal = true)
  void setSession(Session session);

}
