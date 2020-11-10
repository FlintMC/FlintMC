package net.flintmc.launcher.classloading;

import net.flintmc.launcher.LaunchController;
import net.flintmc.launcher.classloading.common.ClassInformation;
import net.flintmc.launcher.classloading.common.CommonClassLoader;
import net.flintmc.launcher.classloading.common.CommonClassLoaderHelper;
import net.flintmc.launcher.service.LauncherPlugin;
import net.flintmc.transform.exceptions.ClassTransformException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

/**
 * Main class loader for applications launched using a {@link LaunchController}.
 */
public class RootClassLoader extends URLClassLoader implements CommonClassLoader {
  static {
    ClassLoader.registerAsParallelCapable();
  }

  private final Set<String> currentlyLoading;
  private final Set<LauncherPlugin> plugins;
  private final List<ChildClassLoader> children;
  private final List<String> modificationExclusions;
  private final Map<String, Class<?>> classCache;
  private final Map<URL, byte[]> resourceDataCache;
  private final Logger logger;
  private final Map<String, byte[]> modifiedClasses;
  private boolean transformEnabled;

  /**
   * Constructs a new instance of the root class loader with the specified set of URL's as the class
   * path.
   *
   * @param urls The new class path of this class loader
   */
  public RootClassLoader(URL[] urls) {
    super(urls, null);
    this.currentlyLoading = new HashSet<>();
    this.plugins = new HashSet<>();
    this.children = new ArrayList<>();
    this.modificationExclusions = new ArrayList<>();
    this.classCache = new ConcurrentHashMap<>();
    this.resourceDataCache = new ConcurrentHashMap<>();
    this.logger = LogManager.getLogger(RootClassLoader.class);

    this.transformEnabled = false;

    this.modifiedClasses = new HashMap<>();

    // We don't want those packages to be transformed,
    // they either are Java/JVM internals or launcher parts
    excludeFromModification("java.");
    excludeFromModification("javax.");
    excludeFromModification("com.sun.");
    excludeFromModification("net.flintmc.launcher.");
  }

  public void addModifiedClass(String name, byte[] byteCode) {
    this.modifiedClasses.put(name, byteCode);
  }

  /**
   * Registers new plugins in the root class loader. As soon as a plugin is registered it will be
   * able to intercept class loading and apply transforms.
   *
   * @param plugins The plugins to register
   */
  public void addPlugins(Collection<LauncherPlugin> plugins) {
    this.plugins.addAll(plugins);
  }

  /**
   * Adds URL's to the classpath, they may be used immediately after adding.
   *
   * @param targets URL's to add to the classpath
   */
  public void addURLs(Collection<URL> targets) {
    targets.forEach(this::addURL);
  }

  /**
   * Prepares this {@link RootClassLoader} for launching. This will call the {@link
   * LauncherPlugin#configureRootLoader(RootClassLoader)} method on every registered plugin.
   */
  public void prepare() {
    if (transformEnabled) {
      throw new IllegalStateException("This RootClassLoader is prepared already");
    }

    plugins.forEach(plugin -> plugin.configureRootLoader(this));
    transformEnabled = true;
  }

  /**
   * Marks a specified set of prefixes as excluded from transformation. For example, {@code "a.b."}
   * would exclude everything in the package a.b and its subpackages.
   *
   * @param names The name prefixes of the packages to exclude from transformation
   */
  public void excludeFromModification(String... names) {
    modificationExclusions.addAll(Arrays.asList(names));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> findClass(String name) throws ClassNotFoundException {
    return findClass(name, null);
  }

  /**
   * Extension of {@link ClassLoader#findClass(String)} providing the same semantics except allowing
   * another loader to be preferred. The class is guaranteed to be used from the preferred loader if
   * the following conditions are met: 1. The class has not been loaded already 2. The class has not
   * been excluded from transformations 3. The class is not an internal class 4. Transformation has
   * been enabled already 5. The preferred loader is able to find the class
   *
   * @param name            The name of the class to find
   * @param preferredLoader The loader the class should be searched from first, or null if no loader
   *                        has a preferred role
   * @return The found class
   * @throws ClassNotFoundException If the class can't be found or an exception occurs finding the
   *                                class
   * @throws IllegalStateException  If the class is being searched already
   */
  public Class<?> findClass(String name, ChildClassLoader preferredLoader)
      throws ClassNotFoundException {
    if (currentlyLoading.contains(name)) {
      throw new IllegalStateException("Circular load detected: " + name);
    }

    // Filter out internal classes
    if (name.equals(RootClassLoader.class.getName())) {
      return RootClassLoader.class;
    } else if (name.equals(LauncherPlugin.class.getName())) {
      return LauncherPlugin.class;
    } else if (name.startsWith("net.flintmc.launcher.classloading.")) {
      return Class.forName(name, false, RootClassLoader.class.getClassLoader());
    }

    // Reuse cached classes
    if (classCache.containsKey(name)) {
      return classCache.get(name);
    } else if (!transformEnabled || modificationExclusions.stream().anyMatch(name::startsWith)) {
      // If transformation has not been enabled or the class is excluded from transformation,
      // delegate loading to the URL class loader implementation and cache the result
      Class<?> clazz = super.findClass(name);
      classCache.put(name, clazz);
      return clazz;
    }

    // Mark the class as currently being loaded to prevent race conditions
    currentlyLoading.add(name);

    try {
      // Initially set the loader to the preferred loader (or null if not set)
      CommonClassLoader loader = preferredLoader;
      ClassInformation information = null;

      if (loader != null) {
        // If a preferred classloader has been set, try to get the class bytes
        // using its resource access
        information = CommonClassLoaderHelper.retrieveClass(loader, name);
      }

      // Search all child loaders until we either:
      // A. found the class
      // B. have no children left to search
      for (Iterator<ChildClassLoader> it = children.iterator();
           information == null && it.hasNext(); ) {
        loader = it.next();
        information = CommonClassLoaderHelper.retrieveClass(loader, name);
      }

      if (information == null) {
        // We still have not found anything, try to find the class on our classpath
        loader = this;
        information = CommonClassLoaderHelper.retrieveClass(loader, name);

        if (information == null) {
          // Class not found at all, bail out
          logger.trace(
              "Failed to find class {} after searching root and the following children: [{}]",
              () -> name,
              () ->
                  children.stream()
                      .map(CommonClassLoader::getClassloaderName)
                      .collect(Collectors.joining(", ")));
          throw new ClassNotFoundException(
              "Class "
                  + name
                  + " not found on classpath (searched root and "
                  + children.size()
                  + " child loaders)");
        }
      }
      // Retrieve the raw class bytes
      byte[] classBytes = this.modifiedClasses.getOrDefault(name, information.getClassBytes());

      for (LauncherPlugin plugin : plugins) {
        byte[] newBytes = plugin.modifyClass(name, classBytes);
        if (newBytes != null) {
          // The plugin has modified the bytes, copy the reference of the array over
          classBytes = newBytes;
        }
      }

      // Let the used loader define the class and cache the result
      CodeSource codeSource =
          new CodeSource(information.getResourceURL(), information.getSigners());
      Class<?> clazz = loader.commonDefineClass(name, classBytes, 0, classBytes.length, codeSource);
      classCache.put(name, clazz);

      return clazz;
    } catch (IOException exception) {
      throw new ClassNotFoundException(
          "Failed to find class " + name + " due to IOException", exception);
    } catch (ClassTransformException exception) {
      throw new ClassNotFoundException(
          "Unable to find class " + name + " due to ClassTransformException", exception);
    } finally {
      // Make sure we remove the loading flag from the class
      currentlyLoading.remove(name);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL findResource(String name) {
    return findResource(name, true);
  }

  /**
   * Extension of {@link ClassLoader#findResource(String)} allowing to disable redirects by launch
   * plugins.
   *
   * @param name          The name of the resource to find
   * @param allowRedirect Wether child plugins should be allowed to redirect the URL to a new one
   * @return An {@link URL} to the found resource, or {@code null} if the resource could not be
   * found
   * @see ClassLoader#findResource(String)
   */
  public URL findResource(String name, boolean allowRedirect) {
    try {
      // Find all available resources matching this name
      Enumeration<URL> allWithName = findResources(name, allowRedirect);
      if (!allWithName.hasMoreElements()) {
        // There are no such resources with the name given, abort early
        return null;
      }

      // Find the first available resources matching the name
      URL first = allWithName.nextElement();
  /*    if (allWithName.hasMoreElements()) {
        // We have multiple resources with the same name, compare them by comparing their contents
        // Always use the first resource as a reference
        byte[] firstData;
        if (resourceDataCache.containsKey(first)) {
          firstData = resourceDataCache.get(first);
        } else {
          firstData = CommonClassLoaderHelper.readResource(first.openConnection());
          resourceDataCache.put(first, firstData);
        }

        // Iterate over every resource with the same name
        while (allWithName.hasMoreElements()) {
          URL next = allWithName.nextElement();
          byte[] nextData;

          // Look up the data in the resource cache
          if (resourceDataCache.containsKey(next)) {
            nextData = resourceDataCache.get(next);
          } else {
            nextData = CommonClassLoaderHelper.readResource(next.openConnection());
            resourceDataCache.put(next, nextData);
          }

          // Compare them and handle a mismatch
          if (!Arrays.equals(firstData, nextData)) {
            // TODO: Currently, we have classpath conflicts, so this needs to be fixed first,
            //       then re-enable the throw!
            *//*logger.warn("Resources with same name but different content found: ");
            logger.warn("\t{}", first.toExternalForm());
            logger.warn("\t{}", next.toExternalForm());*//*
            // throw new UnsupportedOperationException("Resources with same name but different
            // content found:\n" +
            //     "\t" + first.toExternalForm() +"\n\t" + next.toExternalForm());
          }
        }
      }*/

      return first;
    } catch (IOException exception) {
      logger.warn("IOException while trying to retrieve resource " + name, exception);
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Enumeration<URL> findResources(String name) throws IOException {
    return findResources(name, true);
  }

  /**
   * Extension of {@link ClassLoader#findResources(String)} allowing to disable redirects by launch
   * plugins.
   *
   * @param name          The name of the resource to found
   * @param allowRedirect Wether child plugins should be allowed to redirect the URL to a new one
   * @return An enumeration of URL's pointing to resources matching the given name
   * @throws IOException If an I/O error occurs finding the resources
   * @see ClassLoader#findResources(String)
   */
  public Enumeration<URL> findResources(String name, boolean allowRedirect) throws IOException {
    // First search our own classpath
    List<URL> resources = Collections.list(super.findResources(name));
    for (ChildClassLoader childClassLoader : children) {
      // For every child as it for matching resources too
      resources.addAll(Collections.list(childClassLoader.commonFindResources(name)));
    }

    if (allowRedirect) {
      // Redirection has been enabled, process every URL
      List<URL> adjustedResources = new ArrayList<>();
      for (URL suggested : resources) {
        for (LauncherPlugin plugin : plugins) {
          URL newSuggestion = plugin.adjustResourceURL(name, suggested);
          if (newSuggestion != null) {
            // The plugin has applied a redirect, copy the new URL to the suggested one
            suggested = newSuggestion;
          }
        }

        // Write down the final URL
        adjustedResources.add(suggested);
      }

      return Collections.enumeration(adjustedResources);
    } else {
      return Collections.enumeration(resources);
    }
  }

  /**
   * Searches for all available resources and collects them. Resources include class files.
   *
   * @return An enumeration of all available resources
   * @throws IOException        If an I/O error occurs while finding the resources
   * @throws URISyntaxException If an URISyntaxException occurs while finding the resources
   */
  public Enumeration<URL> findAllResources() throws IOException, URISyntaxException {
    List<URL> collected = new ArrayList<>();

    for (URL url : getURLs()) {
      // Scan our own classpath
      collected.addAll(CommonClassLoaderHelper.scanResources(url));
    }

    for (ChildClassLoader child : children) {
      // Request resources from child loaders too
      collected.addAll(Collections.list(child.commonFindAllResources()));
    }

    return Collections.enumeration(collected);
  }

  /**
   * Adds a child classloader which classes will be accessible to the root class loader.
   *
   * @param childClassloader The child loader to add
   */
  public void registerChild(ChildClassLoader childClassloader) {
    if (!transformEnabled) {
      throw new IllegalStateException(
          "ChildClassLoader's can only be registered after transformation has been enabled");
    } else if (children.contains(childClassloader)) {
      return;
    }

    children.add(childClassloader);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Package commonDefinePackage(
      String name,
      String specTitle,
      String specVersion,
      String specVendor,
      String implTitle,
      String implVersion,
      String implVendor,
      URL sealBase) {
    return definePackage(
        name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Package commonDefinePackage(String name, Manifest man, URL url) {
    return definePackage(name, man, url);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> commonDefineClass(String name, byte[] b, int off, int len, CodeSource cs) {
    return defineClass(name, b, off, len, cs);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL commonFindResource(String name, boolean forClassLoad) {
    return findResource(name, !forClassLoad);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Enumeration<URL> commonFindResources(String name) throws IOException {
    return findResources(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Enumeration<URL> commonFindAllResources() throws IOException, URISyntaxException {
    return findAllResources();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Package commonGetPackage(String name) {
    return getPackage(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getClassloaderName() {
    return "RootLoader";
  }
}
