package net.labyfy.component.packages;

import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.jar.JarFile;

public interface PackageClassLoader {

  Class<?> findClass(String name) throws ClassNotFoundException;

  @AssistedFactory(PackageClassLoader.class)
  interface Factory {
    PackageClassLoader create(JarFile file);
  }
}
