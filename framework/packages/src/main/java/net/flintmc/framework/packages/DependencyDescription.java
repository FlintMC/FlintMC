package net.flintmc.framework.packages;

import java.util.List;

/** Represents a dependency a package may have. */
public interface DependencyDescription {

  /**
   * Retrieves the name of the package this dependency refers to.
   *
   * @return The name of the package this dependency refers to
   */
  String getName();

  /**
   * Retrieves the list of versions satisfying the dependency constraint.
   *
   * @return The list of versions satisfying the dependency constraint
   */
  List<String> getVersions();

  /** @return the repository channel this dependency was published in. */
  String getChannel();

  /**
   * Checks if a package satisfies this dependency.
   *
   * @param manifest The {@link PackageManifest} of the package to check
   * @return {@code true}, if the package satisfies this dependency, {@code false} otherwise
   */
  boolean matches(PackageManifest manifest);
}
