package net.flintmc.util.session.internal.refresh;

import net.flintmc.transform.shadow.FieldSetter;
import net.flintmc.transform.shadow.Shadow;

// The default UserAuthentication of the Authlib doesn't support overriding the access token
@Shadow("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication")
public interface RefreshableYggdrasilUserAuthentication {

  @FieldSetter("accessToken")
  void setAccessToken(String accessToken);

}
