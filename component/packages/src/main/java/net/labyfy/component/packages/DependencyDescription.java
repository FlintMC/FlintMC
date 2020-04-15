package net.labyfy.component.packages;

import java.util.List;

/** Represents a dependency a package may have. */
public interface DependencyDescription {

  /** @return the name of the package it depends on. */
  String getName();

  /** @return a list of versions that are acceptable to satisfy the dependency. */
  List<String> getVersions();

  /**
   * Checks if a package can satisfy this dependency.
   *
   * @param description the PackageDescription of the package to check.
   * @return true, if the package satisfies this dependency.
   */
  boolean matches(PackageDescription description);
}
