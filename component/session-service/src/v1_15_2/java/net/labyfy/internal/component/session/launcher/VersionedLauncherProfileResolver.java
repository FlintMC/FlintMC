package net.labyfy.internal.component.session.launcher;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.session.launcher.LauncherProfileResolver;
import net.labyfy.component.session.launcher.LauncherProfiles;
import net.minecraft.client.Minecraft;

@Singleton
@Implement(value = LauncherProfileResolver.class, version = "1.15.2")
public class VersionedLauncherProfileResolver extends DefaultLauncherProfileResolver {

  @Inject
  private VersionedLauncherProfileResolver(LauncherProfiles.Factory profilesFactory) {
    super(profilesFactory, () -> Minecraft.getInstance().gameDir.toPath());
  }

}
