package net.labyfy.component.version;

/**
 * The version helper can be used to write version specific code in the internal module.
 */
public interface VersionHelper {

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
   * Whether the minor version of the client is below the given minor version.
   *
   * @param minor The minor version.
   * @return {@code true} if the minor version of the client is below the given minor version, otherwise {@code false}.
   */
  boolean isUnder(int minor);

  /**
   * Whether the minor and patch versions of the client is below the given minor and patch versions.
   *
   * @param minor The minor version.
   * @param patch The patch version.
   * @return {@code true} if the minor and patch versions of the client is below the given minor and patch versions,
   * otherwise {@code false}.
   */
  boolean isUnder(int minor, int patch);

  /**
   * Whether the versioning of the client is below the given versioning.
   *
   * @param major The major version.
   * @param minor The minor version.
   * @param patch The patch version.
   * @return {@code true} if the client version under the given version numbers, otherwise {@code false}.
   */
  boolean isUnder(int major, int minor, int patch);

  /**
   * Retrieves the current game version.
   *
   * @return The current game version.
   */
  String getVersion();


}
