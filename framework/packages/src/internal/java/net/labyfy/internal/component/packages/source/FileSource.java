package net.labyfy.internal.component.packages.source;

import net.labyfy.component.launcher.classloading.common.CommonClassLoaderHelper;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Represents a jar file as source for a package.
 */
public class FileSource implements PackageSource {
  private final File file;
  private final JarFile jar;

  /**
   * Constructs a new {@link FileSource} with the given file interpreted as a jar file.
   *
   * @param file The jar file of this source
   * @throws IOException If the file could not be read.
   */
  FileSource(File file) throws IOException {
    this.file = file;
    this.jar = new JarFile(file);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() throws IOException {
    jar.close();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL findResource(String path) {
    JarEntry entry = jar.getJarEntry(path);
    if (entry == null) {
      // If there is no entry with the given path, return null
      return null;
    }

    try {
      /*
       * Convert the path into an URL having the following format:
       * /path/to/file.jar!/path/to/the/resource
       *
       * It is important to note that forward slashes are required as the path part of the URL.
       */
      return new URL("jar:file:" + file.getAbsolutePath().replace('\\', '/') + "!/" + path);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Path is invalid", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Enumeration<URL> findResources(String name) {
    // There can't be any duplicates in a jar file, just find a single resource
    // with the given name
    URL resource = findResource(name);
    if (resource != null) {
      // We found a single resource, convert it into an Enumeration
      return new Enumeration<URL>() {
        private URL element = resource;

        /**
         * {@inheritDoc}
         *
         * @implNote Will return {@code true} as long as {@link #nextElement()} has not been called. After that
         * this method will always return {@code false}.
         */
        @Override
        public boolean hasMoreElements() {
          return element != null;
        }

        /**
         * {@inheritDoc}
         *
         * @implNote May only be called once
         */
        @Override
        public URL nextElement() {
          if (!hasMoreElements()) {
            throw new NoSuchElementException("The element if this singleton enumeration has been consumed already");
          }

          // Save and null out the element to indicate that this
          // enumeration has been used
          URL el = element;
          element = null;
          return el;
        }
      };
    } else {
      // No resource with the given name found, return an empty enumeration
      return Collections.emptyEnumeration();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Enumeration<URL> findAllResources() throws IOException, URISyntaxException {
    // Use the CommonClassLoaderHelper to simply scan this jar file
    return Collections.enumeration(CommonClassLoaderHelper.scanResources(file.toURI().toURL()));
  }
}
