package net.labyfy.component.launcher.classloading.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSigner;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class CommonClassLoaderHelper {
  private static final Logger LOGGER = LogManager.getLogger(CommonClassLoaderHelper.class);

  public static ClassInformation retrieveClass(CommonClassLoader commonClassLoader, String name) throws IOException {
    URL resourceURL = commonClassLoader.commonFindResource(name.replace('.', '/').concat(".class"), true);
    if(resourceURL == null) {
      return null;
    }

    URLConnection connection = resourceURL.openConnection();
    byte[] classBytes = readResource(connection);

    CodeSigner[] signers = null;
    int lastDotIndex = name.lastIndexOf('.');

    if(lastDotIndex != -1) {
      signers = handleConnection(commonClassLoader, name.substring(0, lastDotIndex), connection);
    }

    return new ClassInformation(resourceURL, classBytes, signers);
  }

  public static byte[] readResource(URLConnection connection) throws IOException {
    try (InputStream stream = connection.getInputStream()) {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      byte[] buffer = new byte[8192];
      int read;
      while ((read = stream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, read);
      }

      return outputStream.toByteArray();
    }
  }

  private static CodeSigner[] handleConnection(
      CommonClassLoader commonClassLoader, String packageName, URLConnection connection) throws IOException {
    if (connection instanceof JarURLConnection) {
      JarURLConnection jarConnection = (JarURLConnection) connection;
      JarFile jar = jarConnection.getJarFile();

      if (jar != null && jar.getManifest() != null) {
        Manifest manifest = jar.getManifest();
        JarEntry entry = jarConnection.getJarEntry();

        Package pkg = commonClassLoader.commonGetPackage(packageName);
        CodeSigner[] signers = entry.getCodeSigners();

        if (pkg == null) {
          commonClassLoader.commonDefinePackage(packageName, manifest, jarConnection.getJarFileURL());
        } else {
          if (pkg.isSealed() && !pkg.isSealed(jarConnection.getJarFileURL())) {
            LOGGER.warn("Jar {} defines a seal for the already sealed package {}", jar.getName(), packageName);
          } else if (isSealedSet(packageName, manifest)) {
            LOGGER.warn("Jar {} has a security seal for {}, but the package is already defined without a seal",
                jar.getName(), packageName);
          }
        }

        return signers;
      }
    } else {
      Package pkg = commonClassLoader.commonGetPackage(packageName);

      if (pkg == null) {
        commonClassLoader.commonDefinePackage(packageName, null, null, null, null, null, null, null);
      } else if (pkg.isSealed()) {
        LOGGER.warn("The url {} is defining a package for the sealed path {}", connection.getURL(), packageName);
      }
    }

    return null;
  }

  private static boolean isSealedSet(String path, Manifest manifest) {
    Attributes attributes = manifest.getAttributes(path);
    String sealedData = null;

    if (attributes != null) {
      sealedData = attributes.getValue(Attributes.Name.SEALED);
    }

    if (sealedData == null) {
      attributes = manifest.getMainAttributes();

      if (attributes != null) {
        sealedData = attributes.getValue(Attributes.Name.SEALED);
      }
    }

    return sealedData != null && Boolean.parseBoolean(sealedData.toLowerCase());
  }
}
