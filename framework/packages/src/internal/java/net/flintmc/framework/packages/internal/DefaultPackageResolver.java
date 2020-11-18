package net.flintmc.framework.packages.internal;

import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.packages.Package;
import net.flintmc.framework.packages.PackageClassLoader;
import net.flintmc.framework.packages.PackageResolver;

@Implement(PackageResolver.class)
public class DefaultPackageResolver implements PackageResolver {
  @Override
  public Package resolvePackage(Class<?> clazz) {
    ClassLoader classLoader = clazz.getClassLoader();
    if (classLoader instanceof PackageClassLoader) {
      return ((PackageClassLoader) classLoader).getOwner();
    }
    return null;
  }
}
