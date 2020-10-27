package net.flintmc.util.session.internal.refresh;

import com.mojang.authlib.GameProfile;
import net.flintmc.transform.shadow.FieldSetter;
import net.flintmc.transform.shadow.Shadow;

// The default UserAuthentication of the Authlib doesn't support overriding the selected profile, it only does this when logging in/out
@Shadow("com.mojang.authlib.BaseUserAuthentication")
public interface RefreshableBaseUserAuthentication {

  @FieldSetter(value = "selectedProfile")
  void setPublicSelectedProfile(GameProfile profile);

}
