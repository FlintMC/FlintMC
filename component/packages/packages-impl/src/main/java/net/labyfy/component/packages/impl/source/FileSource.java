package net.labyfy.component.packages.impl.source;

import net.labyfy.component.launcher.classloading.common.CommonClassLoaderHelper;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileSource implements PackageSource {
  private final File file;
  private final JarFile jar;

  FileSource(File file) {
    try {
      this.file = file;
      this.jar = new JarFile(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() throws IOException {
    jar.close();
  }

  @Override
  public URL findResource(String path) {
    JarEntry entry = jar.getJarEntry(path);
    if (entry == null) {
      return null;
    }

    try {
      return new URL("jar:file:" + file.getAbsolutePath().replace('\\', '/') + "!/" + path);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Path is invalid", e);
    }
  }

  @Override
  public Enumeration<URL> findResources(String name) {
    URL resource = findResource(name);
    if(resource != null) {
      return Collections.enumeration(Collections.singleton(resource));
    } else {
      return Collections.emptyEnumeration();
    }
  }

  @Override
  public Enumeration<URL> findAllResources() throws IOException {
    return Collections.enumeration(CommonClassLoaderHelper.scanResources(file.toURI().toURL()));
  }
}
