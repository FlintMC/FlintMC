package net.labyfy.downloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JarClassLoader extends URLClassLoader {

  private static final Set<JarClassLoader> PLUGIN_CLASS_LOADERS = new HashSet<>();
  private static final Lock LOCK = new ReentrantLock();
  private static ClassLoader classLoader;

  public JarClassLoader(URL... urls) {
    super(urls);
    try {
      LOCK.lock();
      PLUGIN_CLASS_LOADERS.add(this);
    } finally {
      LOCK.unlock();
    }
  }

  public void unload() {
    try {
      LOCK.lock();
      super.close();
      PLUGIN_CLASS_LOADERS.remove(this);
    } catch (Exception ignored) {
    } finally {
      LOCK.unlock();
    }
  }

  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    try {
      return super.loadClass(name, resolve);
    } catch (ClassNotFoundException ignored) {
    }
    for (JarClassLoader pluginClassLoader : PLUGIN_CLASS_LOADERS) {
      if (pluginClassLoader != this) {
        try {
          return pluginClassLoader.loadClass(name, resolve);
        } catch (ClassNotFoundException ignored) {
        }
      }
    }
    try {
      return classLoader.loadClass(name);
    } catch (ClassNotFoundException ignored) {
    }
    throw new ClassNotFoundException(name);
  }

  static {
    ClassLoader.registerAsParallelCapable();
    classLoader = ClassLoader.getSystemClassLoader();
  }
}
