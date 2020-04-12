package net.labyfy.component.packages;

import net.labyfy.component.inject.assisted.AssistedFactory;

import java.io.File;

public interface Package {

  /** @return the name of this package */
  String getName();

  /** @return the version of this package */
  String getVersion();

  /** @return the current state of this package */
  PackageState getState();

  /**
   * Sets the state of the package. The previous state must be NOT_LOADED and the state can't be
   * explicitly set to LOADED.
   *
   * @param state the new state for the package
   */
  void setState(PackageState state);

  /**
   * Tries to load this package. Package must be in the UNLOADED state. This call should not throw
   * exceptions caused by a failed load attempt but only exceptions caused by unmet preconditions
   * (e.g. wrong state).
   *
   * @return the state of the package after the load attempt
   */
  PackageState load();

  /**
   * If an exception occurred during the load attempt, this getter will return that exception. The
   * package state must be ERRORED.
   *
   * @return the exception that occurred
   */
  Exception getLoadException();

  @AssistedFactory(Package.class)
  interface Factory {
    Package create(File jarFile);
  }
}
