package net.labyfy.component.packages;

/** Represents the state of a package. */
public enum PackageState {

  /** The classes of the package are available through the package class loader. */
  LOADED,
  /** The packages autoload classes have been submitted to the ServiceRepository. */
  ENABLED,
  /** The packages classes are not available through a classloader yet, but it can be loaded. */
  NOT_LOADED,
  /** The packages package.json manifest is present, but not valid. */
  INVALID_DESCRIPTION,
  /** The current version of Labyfy is not compatible with this package. */
  LABYFY_NOT_COMPATIBLE,
  /** The package is not compatible with the current Minecraft version. */
  MINECRAFT_NOT_COMPATIBLE,
  /** The package has dependencies that are not satisfiable in the current configuration. */
  UNSATISFIABLE_DEPENDENCIES,
  /** There is already another package loaded that is conflicting with this package. */
  CONFLICTING_PACKAGE_LOADED,
  /** An error occurred while trying to load this package. */
  ERRORED;

  /**
   * Checks if the given package's state matches this state.
   *
   * @param pack the package to check.
   * @return true, if package's state equals this.
   */
  public boolean matches(Package pack) {
    return this.equals(pack.getState());
  }
}
