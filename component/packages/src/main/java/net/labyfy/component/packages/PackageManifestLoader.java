package net.labyfy.component.packages;

import java.util.Optional;
import java.util.jar.JarFile;

/**
 * Responsible for reading the package manifest out of a given jar file.
 */
public interface PackageManifestLoader {
  /**
   * Checks if a package.json manifest is present it the given JarFile, but doesn not guarantee that
   * it contains a valid package manifest.
   *
   * @param file The JarFile to check for a manifest
   * @return {@code true} if a manifest is present, {@code false} otherwise
   */
  boolean isManifestPresent(JarFile file);

  /**
   * Tries to load the package manifest from the available manifest file in the given jar. Will
   * fail, if the manifest is not present or readable. This method does not check if the manifests
   * content is valid, use the {@link PackageManifest#isValid()} method for that.
   *
   * @param file the JarFile in which the manifest can be found.
   * @return The package manifest if a package manifest was parsed, {@link Optional#empty()} otherwise.
   */
  Optional<PackageManifest> loadManifest(JarFile file);
}
