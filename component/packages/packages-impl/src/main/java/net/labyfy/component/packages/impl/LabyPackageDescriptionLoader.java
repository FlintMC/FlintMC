package net.labyfy.component.packages.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.packages.PackageDescription;
import net.labyfy.component.packages.PackageDescriptionLoader;

import java.io.File;

@Singleton
@Implement(PackageDescriptionLoader.class)
public class LabyPackageDescriptionLoader implements PackageDescriptionLoader {

  @Inject
  private LabyPackageDescriptionLoader() {}

  @Override
  public boolean isDescriptionPresent(File file) {
    return false;
  }

  @Override
  public PackageDescription loadDescription(File file) {
    return null;
  }
}
