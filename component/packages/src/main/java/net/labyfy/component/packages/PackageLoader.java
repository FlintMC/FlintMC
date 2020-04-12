package net.labyfy.component.packages;

import java.util.List;

/**
 * This class is used to instantiate the package loader which is used to resolve and load the
 * packages and their dependencies
 */
public interface PackageLoader {

  List<Package> getAllPackages();

  List<Package> getLoadedPackages();
}
