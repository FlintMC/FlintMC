package net.labyfy.component.packages;

import net.labyfy.component.inject.assisted.AssistedFactory;

import java.io.File;

public interface PackageClassLoader {

  Class<?> findClass(String name) throws ClassNotFoundException;

  ClassLoader asClassLoader();

  @AssistedFactory(PackageClassLoader.class)
  interface Factory {
    PackageClassLoader create(File file);
  }
}
