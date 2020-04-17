package net.labyfy.component.packages;

import net.labyfy.component.inject.assisted.AssistedFactory;

import java.io.File;

/** Responsible for making classes from a package available as a Class reference. */
public interface PackageClassLoader {

  /**
   * Tries to resolve a Class by it's name from the package.
   *
   * @param name the name of the class.
   * @return a Class<?> reference.
   * @throws ClassNotFoundException if the class could not be resolved.
   */
  Class<?> findClass(String name) throws ClassNotFoundException;

  /**
   * Unwraps the plain Java ClassLoader reference that is used for defining those classes.
   *
   * @return a plain Java ClassLoader.
   */
  ClassLoader asClassLoader();

  /**
   * Retrieves the package owning this classloader.
   *
   * @return the package
   */
  Package getOwner();

  @AssistedFactory(PackageClassLoader.class)
  interface Factory {
    /**
     * Instantiates the currently used implementation for this interface.
     *
     * @param owner the package this classloader should be created for.
     * @return a new PackageClassLoader.
     */
    PackageClassLoader create(Package owner);
  }
}
