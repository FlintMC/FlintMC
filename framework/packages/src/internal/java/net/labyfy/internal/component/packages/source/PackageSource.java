package net.labyfy.internal.component.packages.source;

import net.labyfy.component.packages.Package;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Represents a location a package can be access with. Currently package sources are
 * considered to be an implementation detail.
 */
public interface PackageSource extends AutoCloseable {
  /**
   * {@inheritDoc}
   */
  @Override
  default void close() throws IOException {
  }

  /**
   * Locates a resource within the resources accessible with this source.
   *
   * @param path The path of the resource to find
   * @return An {@link URL} pointing to the found resource, or null if not found
   */
  URL findResource(String path);

  /**
   * Locates all resources matching the given name accessible with this source.
   *
   * @param name The name of the resources to find
   * @return An enumeration of all resources matching the given name accessible with this source
   * @throws IOException If an I/O error occurred while finding the resources
   */
  Enumeration<URL> findResources(String name) throws IOException;

  /**
   * Locates all resource accessible with this source.
   *
   * @return An enumeration of all resources accessible with this source
   * @throws IOException If an I/O error occurred while locating the resources
   * @throws URISyntaxException If an URISyntaxException occurred while locating the resource.
   */
  Enumeration<URL> findAllResources() throws IOException, URISyntaxException;

  /**
   * Creates a package source matching the given package.
   *
   * @param pkg The package to create the source for
   * @return The created source which can be used to access resources of the package
   * @throws IOException If the jar file could not be read.
   */
  static PackageSource of(Package pkg) throws IOException {
    if (pkg.getFile() == null) {
      return new ClasspathSource();
    } else {
      return new FileSource(pkg.getFile());
    }
  }
}
