package net.labyfy.component.version;

/**
 * The version helper can be used to write version specific code in the internal module.
 */
public interface VersionHelper {

  /**
   * Whether the minor version is lower than 13.
   *
   * @return {@code true} if the minor version is lower than 13, otherwise {@code false}.
   */
  boolean isUnder13();

  /**
   * Whether the minor version is lower than 16.
   *
   * @return {@code true} if the minor version is lower than 16, otherwise {@code false}.
   */
  boolean isUnder16();

  /**
   * Retrieves the major version.
   *
   * @return The major version.
   */
  int getMajor();

  /**
   * Retrieves the minor version.
   *
   * @return The minor version.
   */
  int getMinor();

  /**
   * Retrieves the patch version.
   *
   * @return The patch version.
   */
  int getPatch();

  /**
   * Retrieves an array with the version split in it.<br>
   * <b>Note:</b> The array has a size of {@code 3}<br>
   * The first index of the array is the major version, the minor version is the second index and the third index is the patch version.
   * <br>
   * The correct version format would be this one "<strong>MAJOR.MINOR.PATCH</strong>" but this would not be wrong either "<strong>MAJOR.MINOR</strong>"
   *
   * @return An array with the version split in it.
   */
  int[] getVersioning();

  /**
   * Retrieves the current game version.
   *
   * @return The current game version.
   */
  String getVersion();


}
