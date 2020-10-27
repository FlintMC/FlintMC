package net.flintmc.util.session.v1_15_2.launcher;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.session.internal.launcher.DefaultLauncherProfileResolver;
import net.flintmc.util.session.launcher.LauncherProfileResolver;
import net.flintmc.util.session.launcher.LauncherProfiles;
import net.minecraft.client.Minecraft;

@Singleton
@Implement(value = LauncherProfileResolver.class, version = "1.15.2")
public class VersionedLauncherProfileResolver extends DefaultLauncherProfileResolver {

  @Inject
  private VersionedLauncherProfileResolver(LauncherProfiles.Factory profilesFactory) {
    super(profilesFactory, () -> Minecraft.getInstance().gameDir.toPath());
  }

}
