package net.labyfy.internal.component.session;

import net.labyfy.component.transform.shadow.FieldSetter;
import net.labyfy.component.transform.shadow.Shadow;
import net.minecraft.util.Session;

@Shadow("net.minecraft.client.Minecraft")
public interface SessionRefreshableMinecraft {

  @FieldSetter(value = "session", removeFinal = true)
  void setSession(Session session);

}
