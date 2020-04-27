package net.labyfy.component.launcher.classloading.common;

import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * This is a helper interface to share logic between {@link ClassLoader} instances
 */
public interface CommonClassLoader {
  /**
   * Method bridge for the protected method `definePackage` in {@link ClassLoader}
   */
  Package commonDefinePackage(String name, String specTitle,
                              String specVersion, String specVendor,
                              String implTitle, String implVersion,
                              String implVendor, URL sealBase);

  /**
   * Method bridge for the protected method `definePackage` in {@link URLClassLoader}.
   * This method has to be overwritten of the implementing classloader is an
   * {@link URLClassLoader}, else it may be left as is.
   */
  default Package commonDefinePackage(String name, Manifest man, URL url) {
    if(this instanceof URLClassLoader) {
      throw new AssertionError("Expected commonDefinePackage(String name, Manifest man, URL url) to" +
          " be overwritten, since the instance implementing this interface is an URLClassLoader");
    }

    String specificationTitle = null;
    String specificationVersion = null;
    String specificationVendor = null;
    String implementationTitle = null;
    String implementationVersion = null;
    String implementationVendor = null;

    URL sealBase = null;
    boolean sealed = false;

    Attributes attr = man.getMainAttributes();
    if(attr != null) {
      specificationTitle = attr.getValue(Attributes.Name.SPECIFICATION_TITLE);
      specificationVersion = attr.getValue(Attributes.Name.SPECIFICATION_VERSION);
      specificationVendor = attr.getValue(Attributes.Name.SPECIFICATION_VENDOR);
      implementationTitle = attr.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
      implementationVersion = attr.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
      implementationVendor = attr.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
      sealed = "true".equalsIgnoreCase(attr.getValue(Attributes.Name.SEALED));
    }

    if(sealed) {
      sealBase = url;
    }

    return commonDefinePackage(
        name,
        specificationTitle,
        specificationVersion,
        specificationVendor,
        implementationTitle,
        implementationVersion,
        implementationVendor,
        sealBase
    );
  }

  /**
   * Method bridge for the protected `defineClass` method in {@link ClassLoader}
   */
  Class<?> commonDefineClass(String name, byte[] b, int off, int len, CodeSource cs);

  /**
   * Method bridge for the protected `findResource` method in {@link ClassLoader}
   * Additionally, the parameter `forClassLoad` has been added.
   *
   * @param forClassLoad true, if the searched resource will be used as for classloading.
   *                     Usually, this will be true, when name ends with `".class"`
   */
  URL commonFindResource(String name, boolean forClassLoad);

  /**
   * Method bridge for the protected `getPackage` method in {@link ClassLoader}
   */
  Package commonGetPackage(String name);

  /**
   * Retrieves a short, descriptive name for this ClassLoader, for example
   * `RootLoader`, or `PackageLoader{TestPackage}`
   *
   * @return the name of this classloader
   */
  String getClassloaderName();
}