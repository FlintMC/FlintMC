package net.labyfy.component.launcher.classloading;

import net.labyfy.component.launcher.service.LabyLauncherPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class RootClassloader extends URLClassLoader {
  private final Logger logger;
  private final List<LabyLauncherPlugin> plugins;
  private final List<String> modificationExclusions;
  private final Map<String, Class<?>> classCache;

  public RootClassloader(URL[] urls, List<LabyLauncherPlugin> plugins) {
    super(urls, null);
    this.logger = LogManager.getLogger(RootClassloader.class);
    this.plugins = plugins;
    this.modificationExclusions = new ArrayList<>();
    this.classCache = new WeakHashMap<>();

    excludeFromModification("net.labyfy.component.launcher.");
  }

  public void prepare() {
    plugins.forEach(plugin -> plugin.configureRootLoader(this));
  }

  public void excludeFromModification(String... names) {
    modificationExclusions.addAll(Arrays.asList(names));
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    if (classCache.containsKey(name)) {
      return classCache.get(name);
    } else if (modificationExclusions.stream().anyMatch(name::startsWith)) {
      Class<?> clazz = super.findClass(name);
      classCache.put(name, clazz);
      return clazz;
    }

    URL classUrl = findResource(name.replace('.', '/').concat(".class"), false);
    if (classUrl == null) {
      throw new ClassNotFoundException("Class with the name " + name + " not found in classpath");
    }

    URLConnection connection;
    try {
      connection = classUrl.openConnection();
    } catch (IOException e) {
      throw new ClassNotFoundException("Failed to load class due to IOException while opening connection", e);
    }

    byte[] classData;

    try {
      classData = readClass(connection);
    } catch (IOException e) {
      throw new ClassNotFoundException("Failed to load class due to IOException while reading data", e);
    }

    int lastDotIndex = name.lastIndexOf('.');
    CodeSigner[] signers;
    if (lastDotIndex != -1) {
      try {
        signers = handleClassConnection(name.substring(0, lastDotIndex), connection);
      } catch (IOException e) {
        throw new ClassNotFoundException("Failed to load class due to IOException while handling connection", e);
      }
    } else {
      signers = null;
    }

    for (LabyLauncherPlugin plugin : plugins) {
      byte[] newData = plugin.modifyClass(classData);
      if (newData != null) {
        classData = newData;
      }
    }


    CodeSource codeSource = new CodeSource(classUrl, signers);
    Class<?> clazz = defineClass(name, classData, 0, classData.length, codeSource);
    classCache.put(name, clazz);
    return clazz;

  }

  @Override
  public URL findResource(String name) {
    return findResource(name, true);
  }

  private URL findResource(String name, boolean allowRedirect) {
    if (allowRedirect) {
      URL suggested = super.findResource(name);
      for (LabyLauncherPlugin plugin : plugins) {
        URL newSuggestion = plugin.adjustResourceURL(name, suggested);
        if (newSuggestion != null) {
          suggested = newSuggestion;
        }
      }

      return suggested;
    }

    return super.findResource(name);
  }

  private byte[] readClass(URLConnection connection) throws IOException {
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

  private CodeSigner[] handleClassConnection(String packageName, URLConnection connection) throws IOException {
    if (connection instanceof JarURLConnection) {
      JarURLConnection jarConnection = (JarURLConnection) connection;
      JarFile jar = jarConnection.getJarFile();

      if (jar != null && jar.getManifest() != null) {
        Manifest manifest = jar.getManifest();
        JarEntry entry = jarConnection.getJarEntry();

        Package pkg = getPackage(packageName);
        CodeSigner[] signers = entry.getCodeSigners();

        if (pkg == null) {
          definePackage(packageName, manifest, jarConnection.getJarFileURL());
        } else {
          if (pkg.isSealed() && !pkg.isSealed(jarConnection.getJarFileURL())) {
            logger.warn("Jar {} defines a seal for the already sealed package {}", jar.getName(), packageName);
          } else if (sealedSet(packageName, manifest)) {
            logger.warn("Jar {} has a security seal for {}, but the package is already defined without a seal", jar.getName(), packageName);
          }
        }

        return signers;
      }
    } else {
      Package pkg = getPackage(packageName);

      if (pkg == null) {
        definePackage(packageName, null, null, null, null, null, null, null);
      } else if (pkg.isSealed()) {
        logger.warn("The url {} is defining a package for the sealed path {}", connection.getURL(), packageName);
      }
    }

    return null;
  }

  private boolean sealedSet(String path, Manifest manifest) {
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
