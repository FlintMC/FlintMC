package net.labyfy.component.stereotype;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.util.Map;

/**
 * The version helper can be used to write version specific code in the internal module.
 */
@Singleton
public class VersionHelper {

  private final Map<String, String> launchArguments;

  @Inject
  public VersionHelper(@Named("launchArguments") Map launchArguments) {
    this.launchArguments = launchArguments;
  }

  /**
   * Whether the minor version is lower than 13.
   *
   * @return {@code true} if the minor version is lower than 13, otherwise {@code false}.
   */
  public boolean isUnder13() {
    return this.getMinor() < 13;
  }

  /**
   * Whether the minor version is lower than 16.
   *
   * @return {@code true} if the minor version is lower than 16, otherwise {@code false}.
   */
  public boolean isUnder16() {
    return this.getMinor() < 16;
  }

  /**
   * Retrieves the major version.
   *
   * @return The major version.
   */
  public int getMajor() {
    return this.getVersioning()[0];
  }

  /**
   * Retrieves the minor version.
   *
   * @return The minor version.
   */
  public int getMinor() {
    return this.getVersioning()[1];
  }

  /**
   * Retrieves the patch version.
   *
   * @return The patch version.
   */
  public int getPatch() {
    return this.getVersioning()[2];
  }

  /**
   * Retrieves an array with the version split in it.<br>
   * <b>Note:</b> The array has a size of {@code 3}<br>
   * The first index of the array is the major version, the minor version is the second index and the third index is the patch version.
   *
   * @return An array with the version split in it.
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
   * Retrieves the current game version.
   *
   * @return The current game version.
   */
  public String getVersion() {
    return this.launchArguments.get("--game-version");
  }


}
