package net.labyfy.component.launcher.classloading;

import net.labyfy.component.launcher.classloading.common.CommonClassLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Interface allowing other classloader implementations
 * to act as a child of the root classloader.
 */
public interface ChildClassLoader extends CommonClassLoader {
  @Override
  default URL commonFindResource(String name, boolean forClassLoad) {
    return childFindResource(name, forClassLoad);
  }

  /**
   * Searches this classloader for resources.
   * Its counterpart is the protected `findResource` method of {@link ClassLoader}.
   *
   * @param name         the name of the resource to find
   * @param forClassLoad true, if the searched resource will be used as for classloading.
   *                     Usually, this will be true, when name ends with `".class"`
   * @return the found url to the resource, or null if none was found
   */
  URL childFindResource(String name, boolean forClassLoad);
}
