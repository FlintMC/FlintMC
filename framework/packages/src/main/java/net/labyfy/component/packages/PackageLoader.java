package net.labyfy.component.packages;

import java.util.Set;

/**
 * Represents the service responsible for loading packages.
 */
public interface PackageLoader {
  /**
   * Retrieves a set of all packages. This ignores the {@link PackageState} completely, which means this
   * set may for example also contain errored or not loaded packages.
   *
   * @return A set of all packages found by the loader
   */
  Set<Package> getAllPackages();

  /**
   * Retrieves a set of all packages which are in the {@link PackageState#LOADED} or {@link PackageState#ENABLED} state.
   *
   * @return A set of all loaded or enabled packages
   */
  Set<Package> getLoadedPackages();

  void load();
}
