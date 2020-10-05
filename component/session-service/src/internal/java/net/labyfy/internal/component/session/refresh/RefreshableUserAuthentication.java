package net.labyfy.internal.component.session.refresh;

import net.labyfy.component.transform.shadow.FieldSetter;
import net.labyfy.component.transform.shadow.Shadow;

@Shadow("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication")
public interface RefreshableUserAuthentication {

  @FieldSetter("accessToken")
  void setAccessToken(String accessToken);

}
