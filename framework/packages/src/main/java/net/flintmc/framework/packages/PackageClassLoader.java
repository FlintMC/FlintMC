package net.flintmc.framework.packages;

/**
 * Represents a {@link ClassLoader} used for loading a package. Note that you should never cast
 * instances of this interface to {@link ClassLoader}, but rather use the {@link #asClassLoader()}
 * method.
 */
public interface PackageClassLoader {
  /**
   * Tries to find a class present in the package.
   *
   * @param name The name of the class to find
   * @return The found class instance
   * @throws ClassNotFoundException If the class could not be found in the package
   */
  Class<?> findClass(String name) throws ClassNotFoundException;

  /**
   * Unwraps the plain Java ClassLoader reference that is used for defining classes within the
   * package owning this class loader.
   *
   * @return The {@link ClassLoader} representation of this {@link PackageClassLoader}
   */
  ClassLoader asClassLoader();

  /**
   * Retrieves the package owning this class loader.
   *
   * @return The package owning this class loader
   */
  Package getOwner();
}
