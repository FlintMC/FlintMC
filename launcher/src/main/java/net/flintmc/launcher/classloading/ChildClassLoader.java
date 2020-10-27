package net.flintmc.launcher.classloading;

import net.flintmc.launcher.classloading.common.CommonClassLoader;

import java.net.URL;

/**
 * Interface allowing other classloader implementations to act as a child of the root classloader.
 * This grants the ability to load transformed classes using a {@link RootClassLoader}.
 */
public interface ChildClassLoader extends CommonClassLoader {
  @Override
  default URL commonFindResource(String name, boolean forClassLoad) {
    return childFindResource(name, forClassLoad);
  }

  /**
   * Searches this classloader for resources. Its counterpart is the protected `findResource` method
   * of {@link ClassLoader}.
   *
   * @param name the name of the resource to find
   * @param forClassLoad true, if the searched resource will be used as for classloading. Usually
   *     this will be true when name ends with {@code ".class"}
   * @return the found url to the resource, or null if none was found
   */
  URL childFindResource(String name, boolean forClassLoad);
}
