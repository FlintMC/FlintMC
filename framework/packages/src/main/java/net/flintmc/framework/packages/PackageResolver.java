package net.flintmc.framework.packages;

/** Resolver to get the {@link Package} in which a class has been loaded. */
public interface PackageResolver {

  /**
   * Resolves the package where the given class has been loaded.
   *
   * @param clazz The non-null class to resolve the package for
   * @return The package of the class or {@code null} if the given class is not loaded in any
   *     package
   */
  Package resolvePackage(Class<?> clazz);
}
