/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.launcher.classloading.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSigner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/** Utility class to share code between instances of {@link CommonClassLoader} instances. */
public class CommonClassLoaderHelper {
  private static final Logger LOGGER = LogManager.getLogger(CommonClassLoaderHelper.class);

  private CommonClassLoaderHelper() {}

  /**
   * Retrieves information and data of a class file from a class loader.
   *
   * @param commonClassLoader The class loader to use to get the data
   * @param name The name of the class
   * @return Information about and content of the class, or {@code null}, if the class could not be
   *     found
   * @throws IOException If an I/O error occurs while reading the class
   */
  public static ClassInformation retrieveClass(CommonClassLoader commonClassLoader, String name)
      throws IOException {
    // Translate the class name to a resource name so we can search for it without
    // an exception being thrown in case the class is not found
    URL resourceURL =
        commonClassLoader.commonFindResource(name.replace('.', '/').concat(".class"), true);
    if (resourceURL == null) {
      // Didn't find the class
      return null;
    }

    // Retrieve the content of the class
    URLConnection connection = resourceURL.openConnection();
    byte[] classBytes = readResource(connection);

    CodeSigner[] signers = null;
    int lastDotIndex = name.lastIndexOf('.');

    if (lastDotIndex != -1) {
      // The class is in a package, try handling signer information
      signers = retrieveSignerData(commonClassLoader, name.substring(0, lastDotIndex), connection);
    }

    return new ClassInformation(resourceURL, classBytes, signers);
  }

  /**
   * Reads all bytes from an {@link URLConnection}.
   *
   * @param connection The connection to read the data from
   * @return The read data
   * @throws IOException If an I/O error occurs while reading the data
   */
  public static byte[] readResource(URLConnection connection) throws IOException {
    try (InputStream stream = connection.getInputStream()) {
      // Buffer our data in a byte array stream
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      byte[] buffer = new byte[8192];
      int read;
      while ((read = stream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, read);
      }

      return outputStream.toByteArray();
    }
  }

  /**
   * Retrieves signer information from a specific {@link URLConnection} and the associated class
   * loader and package.
   *
   * @param commonClassLoader The class loader the connection has originated from
   * @param packageName The package the class can be found in
   * @param connection The connection to retrieve data from
   * @return Extracted signer information, or null if no such information has been found
   * @throws IOException If an I/O error occurs while reading signer data
   */
  private static CodeSigner[] retrieveSignerData(
      CommonClassLoader commonClassLoader, String packageName, URLConnection connection)
      throws IOException {
    if (connection instanceof JarURLConnection) {
      // Jar connections need to be handled in a special way
      JarURLConnection jarConnection = (JarURLConnection) connection;
      JarFile jar = jarConnection.getJarFile();

      if (jar != null && jar.getManifest() != null) {
        // If we have a manifest, try to get signer information from it
        Manifest manifest = jar.getManifest();
        JarEntry entry = jarConnection.getJarEntry();

        // Look up if a package with that name exists already
        Package pkg = commonClassLoader.commonGetPackage(packageName);
        CodeSigner[] signers = entry.getCodeSigners();

        if (pkg == null) {
          // Let the class loader create the requested package
          commonClassLoader.commonDefinePackage(
              packageName, manifest, jarConnection.getJarFileURL());
        } else {
          // Check for security violations, but since we are working in a patched
          // environment just log them
          if (pkg.isSealed() && !pkg.isSealed(jarConnection.getJarFileURL())) {
            LOGGER.warn(
                "Jar {} defines a seal for the already sealed package {}",
                jar.getName(),
                packageName);
          } else if (isSealedSet(packageName, manifest)) {
            LOGGER.warn(
                "Jar {} has a security seal for {}, but the package is already defined without a seal",
                jar.getName(),
                packageName);
          }
        }

        return signers;
      }
    } else {
      // Look up if a package with that name exists already
      Package pkg = commonClassLoader.commonGetPackage(packageName);

      if (pkg == null) {
        // Let the class loader create the requested package without any signer information
        commonClassLoader.commonDefinePackage(
            packageName, null, null, null, null, null, null, null);
      } else if (pkg.isSealed()) {
        // Check for security violations, but since we are working in a patched
        // environment just log them
        LOGGER.warn(
            "The url {} is defining a package for the sealed path {}",
            connection.getURL(),
            packageName);
      }
    }

    return null;
  }

  /**
   * Checks if a path is set as sealed based on a manifest.
   *
   * @param path The path to check
   * @param manifest The manifest to use for information lookup
   * @return {@code true} if the manifest defines the path to be sealed, {@code false} otherwise
   */
  private static boolean isSealedSet(String path, Manifest manifest) {
    Attributes attributes = manifest.getAttributes(path);
    String sealedData = null;

    if (attributes != null) {
      // If the manifest contains path specific attributes, check them
      sealedData = attributes.getValue(Attributes.Name.SEALED);
    }

    if (sealedData == null) {
      // The manifest either didn't contain path specific attributes or
      // didn't define the sealed attribute, check the main attributes
      attributes = manifest.getMainAttributes();

      if (attributes != null) {
        // Main attributes are set, check if they define the file to be sealed
        sealedData = attributes.getValue(Attributes.Name.SEALED);
      }
    }

    // The JVM specification states that the case on the manifest entry
    // is ignored, so just make it always lowercase
    return sealedData != null && Boolean.parseBoolean(sealedData.toLowerCase());
  }

  /**
   * Scans the specified {@link URL} recursively for resources and collects them.
   *
   * @param base The {@link URL} to scan
   * @return A list of found resources
   * @throws IOException If an I/O error occurs while scanning the resources
   * @throws UnsupportedOperationException If the given URL has the {@code "file"} protocol but is
   *     not a file nor a directory
   * @throws URISyntaxException If the URI syntax is invalid.
   */
  public static List<URL> scanResources(URL base) throws IOException, URISyntaxException {
    switch (base.getProtocol()) {
      case "file":
        {
          // File URL, should point to either a file or a directory
          Path path = Paths.get(base.toURI());

          if (!Files.exists(path)) {
            // Looks like the URL points to a nonexistent file, which is valid in case of
            // classpath entries, just return an empty list
            return Collections.emptyList();
          }

          if (Files.isDirectory(path)) {
            // Recursively scan the directory the URL points to
            List<URL> resources = new ArrayList<>();
            for (Path resource : scanDirectory(path)) {
              resources.add(resource.toUri().toURL());
            }

            return resources;
          } else if (Files.isRegularFile(path)) {
            if (path.toString().endsWith(".zip")
                || path.toString().endsWith(".jar")
                || path.toString().endsWith(".war")
                || path.toString().endsWith(".ear")) {
              // The file is in a zip format, scan it as an archive
              return scanZip(path);
            } else {
              // Return the file itself as a resource
              return Collections.singletonList(path.toUri().toURL());
            }
          } else {
            throw new UnsupportedOperationException(
                "Unsupported path " + path + ", not a file nor a directory");
          }
        }

      case "jar:file":
        {
          // File is explicitly specified as a jar
          Path path = Paths.get(base.toURI());

          // Scan the path as archive
          return scanZip(path);
        }

      default:
        // Return the URL itself as a resource
        return Collections.singletonList(base);
    }
  }

  /**
   * Scans a directory recursively and finds all files in it.
   *
   * @param directory The directory to scan
   * @return A list of all found files
   * @throws IOException If an I/O error occurs while scanning the directory
   */
  private static List<Path> scanDirectory(Path directory) throws IOException {
    List<Path> children = Files.list(directory).collect(Collectors.toList());

    List<Path> files = new ArrayList<>();

    for (Path child : children) {
      if (Files.isDirectory(child)) {
        // We found a subdirectory, scan it too
        files.addAll(scanDirectory(child));
      } else {
        files.add(child);
      }
    }

    return files;
  }

  /**
   * Scans a zip file and finds all files in it.
   *
   * @param zipFile The zip file to scan
   * @return A list of URL's each pointing to an entry in the zip file
   * @throws IOException If an I/O error occurs while scanning the zip file
   */
  private static List<URL> scanZip(Path zipFile) throws IOException {
    try (ZipFile file = new ZipFile(zipFile.toFile())) {
      List<URL> collected = new ArrayList<>();

      for (Enumeration<? extends ZipEntry> it = file.entries(); it.hasMoreElements(); ) {
        // Iterate over every entry
        ZipEntry entry = it.nextElement();
        if (entry.isDirectory()) {
          // If we found a directory, simply skip it. The entries in it will appear later
          continue;
        }

        // Add the url to the collected list of URL's by converting foreign file separators
        // to the UNIX format which the URL expects
        collected.add(
            new URL("jar:file:" + zipFile.toString().replace('\\', '/') + "!/" + entry.getName()));
      }

      return collected;
    }
  }
}
