package net.labyfy.internal.component.session.refresh;

import net.labyfy.component.transform.shadow.FieldSetter;
import net.labyfy.component.transform.shadow.Shadow;

// The default UserAuthentication of the Authlib doesn't support overriding the access token
@Shadow("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication")
public interface RefreshableYggdrasilUserAuthentication {

  @FieldSetter("accessToken")
  void setAccessToken(String accessToken);

}
