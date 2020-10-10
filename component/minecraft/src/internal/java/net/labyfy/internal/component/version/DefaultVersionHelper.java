package net.labyfy.internal.component.version;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.version.VersionHelper;

import java.util.Map;

/**
 * Default implementation of the {@link VersionHelper}.
 */
@Singleton
@Implement(value = VersionHelper.class)
public class DefaultVersionHelper implements VersionHelper {

  private final Map<String, String> launchArguments;

  @Inject
  public DefaultVersionHelper(@Named("launchArguments") Map launchArguments) {
    this.launchArguments = launchArguments;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUnder13() {
    return this.getMinor() < 13;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isUnder16() {
    return this.getMinor() < 16;
  }

  /**
   * {@inheritDoc}
   */
  public int getMajor() {
    return this.getVersioning()[0];
  }

  /**
   * {@inheritDoc}
   */
  public int getMinor() {
    return this.getVersioning()[1];
  }

  /**
   * {@inheritDoc}
   */
  public int getPatch() {
    return this.getVersioning()[2];
  }

  /**
   * {@inheritDoc}
   */
  public int[] getVersioning() {
    int[] versioning = new int[3];
    String version = this.getVersion();

    if (!version.contains(".")) {
      throw new IllegalStateException("Not valid version. (" + version + ")");
    }


    String[] split = this.getVersion().split("\\.");

    for (int i = 0; i < split.length; i++) {
      versioning[i] = Integer.parseInt(split[i]);
    }

    // Is only for version without any patches
    if (split.length < 3) {
      versioning[2] = 0;
    }

    return versioning;
  }

  /**
   * {@inheritDoc}
   */
  public String getVersion() {
    return this.launchArguments.get("--game-version");
  }


}
