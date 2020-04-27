package net.labyfy.component.packages;

import java.util.Optional;
import java.util.jar.JarFile;

/** Responsible for reading the package description out of a given jar file. */
public interface PackageDescriptionLoader {

  /**
   * Checks if a package.json manifest is present it the given JarFile, but doesn not guarantee that
   * it contains a valid package description.
   *
   * @param file the JarFile to check.
   * @return true, if a manifest is present.
   */
  boolean isDescriptionPresent(JarFile file);

  /**
   * Tries to load the package description from the available manifest file in the given jar. Will
   * fail, if the manifest is not present or readable.
   *
   * @param file the JarFile in which the manifest can be found.
   * @return Wrapped package description if a valid package description was parsed, Optional.empty()
   *     otherwise.
   */
  Optional<PackageDescription> loadDescription(JarFile file);
}
