package net.labyfy.component.packages;

import net.labyfy.component.inject.assisted.AssistedFactory;

import java.io.File;
import java.util.jar.JarFile;

/** Represents a package at runtime. */
public interface Package {

  /** @return the package description for this package */
  PackageDescription getPackageDescription();

  /** @return the name of this package */
  String getName();

  /** @return the name of this package as it should be displayed to the user */
  String getDisplayName();

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
   * Tries to enable this package by parsing annotations in autoload classes. If an exception occurs
   * the package may stay loaded and become partially enabled.
   */
  void enable();

  /**
   * The package must be in LOADED or ENABLED state.
   *
   * @return the package class loader used for this package
   */
  PackageClassLoader getPackageClassLoader();

  /**
   * If an exception occurred during the load attempt, this getter will return that exception. The
   * package state must be ERRORED.
   *
   * @return the exception that occurred
   */
  Exception getLoadException();

  @AssistedFactory(Package.class)
  interface Factory {
    /**
     * Instantiates the currently used implementation for this interface.
     *
     * @param jarFile the jar file that contains the package to create.
     * @param jar a JarFile reference of the same File.
     * @return a new Package.
     */
    Package create(File jarFile, JarFile jar);
  }
}
