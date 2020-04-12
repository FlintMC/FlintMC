package net.labyfy.component.packages.impl;

import com.google.common.base.Preconditions;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.packages.*;
import net.labyfy.component.packages.Package;

import java.io.File;

@Implement(Package.class)
public class LabyPackage implements Package {

  private File jarFile;
  private PackageDescription packageDescription;
  private PackageClassLoader.Factory classLoaderFactory;
  private PackageState packageState;
  private Exception loadException;

  @AssistedInject
  private LabyPackage(
      PackageDescriptionLoader descriptionLoader,
      PackageClassLoader.Factory classLoaderFactory,
      @Assisted File jarFile) {
    Preconditions.checkNotNull(jarFile);
    Preconditions.checkArgument(
        descriptionLoader.isDescriptionPresent(jarFile),
        "The given JAR File (%s) does not contain a valid package description.",
        jarFile.getName());

    this.jarFile = jarFile;
    this.packageDescription = descriptionLoader.loadDescription(jarFile);
    this.classLoaderFactory = classLoaderFactory;
    this.packageState = PackageState.NOT_LOADED;
    this.loadException = null;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getVersion() {
    return null;
  }

  @Override
  public PackageState getState() {
    return this.packageState;
  }

  @Override
  public void setState(PackageState state) {
    Preconditions.checkState(this.packageState.equals(PackageState.NOT_LOADED));
    Preconditions.checkArgument(
        !state.equals(PackageState.LOADED),
        "The package state can't be explicitly set to LOADED. To get into the LOADED state, you must call the load() method.");

    this.packageState = state;
  }

  @Override
  public PackageState load() {
    Preconditions.checkState(
        this.packageState.equals(PackageState.NOT_LOADED),
        "The package must be in NOT_LOADED state to be loaded.");

        // TODO: find autoload classes and submit them for annotation parsing

    try {
      PackageClassLoader classLoader = this.classLoaderFactory.create(this.jarFile);

      this.packageState = PackageState.LOADED;
    } catch (Exception e) {
      this.packageState = PackageState.ERRORED;
      this.loadException = e;
    }
    return this.packageState;
  }

  @Override
  public Exception getLoadException() {
    Preconditions.checkState(this.packageState.equals(PackageState.ERRORED));
    Preconditions.checkNotNull(this.loadException);
    return this.loadException;
  }
}
