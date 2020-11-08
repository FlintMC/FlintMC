package net.flintmc.framework.packages;

import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;

/** Responsible for reading the package manifest out of a given jar file. */
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
   * @param file The JarFile in which the manifest can be found.
   * @return A loaded package manifest.
   * @throws IOException If the file could not be read.
   */
  PackageManifest loadManifest(JarFile file) throws IOException;

  /**
   * Tries to load the package manifest from the available manifest file at the given url. Will
   * fail, if the manifest is not present or readable. This method does not check if the manifests
   * content is valid, use the {@link PackageManifest#isValid()} method for that.
   *
   * @param  url The url at which the manifest can be found.
   * @return A loaded package manifest.
   * @throws IOException If the file could not be read.
   */
  PackageManifest loadManifest(URL url) throws IOException;
}
