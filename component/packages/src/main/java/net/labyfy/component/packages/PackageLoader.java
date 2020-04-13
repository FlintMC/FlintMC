package net.labyfy.component.packages;

import java.util.Set;

/**
 * This class is used to instantiate the package loader which is used to resolve and load the
 * packages and their dependencies
 */
public interface PackageLoader {

  Set<Package> getAllPackages();

  Set<Package> getLoadedPackages();
}
