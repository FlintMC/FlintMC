package net.labyfy.component.packages;

public enum PackageState {
  LOADED,
  ENABLED,
  NOT_LOADED,
  INVALID_DESCRIPTION,
  LABYFY_NOT_COMPATIBLE,
  MINECRAFT_NOT_COMPATIBLE,
  UNSATISFIABLE_DEPENDENCIES,
  CONFLICTING_PACKAGE_LOADED,
  ERRORED;

  public boolean matches(Package pack) {
    return this.equals(pack.getState());
  }

}
