package net.labyfy.version;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.lang.annotation.Annotation;
import java.util.Map;

public class VersionProvider {
  private final Map<String, String> launchArguments;

  @Inject
  private VersionProvider(@Named("launchArguments") Map<String, String> launchArguments) {
    this.launchArguments = launchArguments;
  }

  public String getMinecraftVersion() {
    return launchArguments.get("--version");
  }

  public static VersionProvider create(Map<String, String> launchArguments) {
    Preconditions.checkNotNull(launchArguments);
    return new VersionProvider(launchArguments);
  }

}
