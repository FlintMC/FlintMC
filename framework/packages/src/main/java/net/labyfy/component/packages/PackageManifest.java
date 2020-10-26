package net.labyfy.component.packages;

import java.util.Set;

/**
 * Represents the metadata of a package.
 */
public interface PackageManifest {

  /**
   * Retrieves the name of the package.
   *
   * @return The name of the package
   */
  String getName();

  /**
   * Retrieves the display name of the package. This will always be a human readable string.
   *
   * @return The display name of the package
   */
  String getDisplayName();

  /**
   * Retrieves the version of the package.
   *
   * @return The version of the package
   */
  String getVersion();

  /**
   * Retrieves the list of authors of this package.
   *
   * @return The list of authors of this package
   */
  Set<String> getAuthors();

  /**
   * Retrieves the description of the package. This will always be a human readable string.
   *
   * @return The description of the package
   */
  String getDescription();

  /**
   * The list of dependencies this package has. This includes `hard` and `soft` dependencies.
   *
   * @return The list of dependencies this package has
   */
  Set<? extends DependencyDescription> getDependencies();

  /**
   * Checks if this package manifest is valid, e.g. if all necessary values are present.
   *
   * @return {@code true} if the manifest is valid, {@code false} otherwise
   */
  boolean isValid();
}
