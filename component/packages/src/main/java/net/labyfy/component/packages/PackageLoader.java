package net.labyfy.component.packages;

import java.util.Set;

/**
 * The implementation should be responsible for loading packages and can later be used to receive
 * packages.
 */
public interface PackageLoader {

  /**
   * Get a set of all packages. This may contain packages that failed to load or have other states
   * than LOADED in general.
   *
   * @return a set of all packages.
   */
  Set<Package> getAllPackages();

  /** @return a set of all loaded or enabled packages. */
  Set<Package> getLoadedPackages();
}
