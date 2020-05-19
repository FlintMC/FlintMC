package net.labyfy.component.launcher.classloading;

import net.labyfy.component.launcher.classloading.common.ClassInformation;
import net.labyfy.component.launcher.classloading.common.CommonClassLoader;
import net.labyfy.component.launcher.classloading.common.CommonClassLoaderHelper;
import net.labyfy.component.launcher.service.LauncherPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

public class RootClassLoader extends URLClassLoader implements CommonClassLoader {
  private final Set<String> currentlyLoading;
  private final Set<LauncherPlugin> plugins;
  private final List<ChildClassLoader> children;
  private final List<String> modificationExclusions;
  private final Map<String, Class<?>> classCache;
  private final Map<URL, byte[]> resourceDataCache;
  private final Logger logger;

  private boolean transformEnabled;

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

    excludeFromModification("java.");
    excludeFromModification("javax.");
    excludeFromModification("com.sun.");
    excludeFromModification("net.labyfy.component.launcher.");
  }

  public void addPlugins(Collection<LauncherPlugin> plugins) {
    this.plugins.addAll(plugins);
  }

  public void addURLs(Collection<URL> targets) {
    targets.forEach(this::addURL);
  }

  public void prepare() {
    plugins.forEach(plugin -> plugin.configureRootLoader(this));
    transformEnabled = true;
  }

  public void excludeFromModification(String... names) {
    modificationExclusions.addAll(Arrays.asList(names));
  }

  @Override
  public Class<?> findClass(String name) throws ClassNotFoundException {
    return findClass(name, null);
  }

  public Class<?> findClass(String name, ChildClassLoader preferredLoader) throws ClassNotFoundException {
    if (currentlyLoading.contains(name)) {
      throw new IllegalStateException("Circular load detected: " + name);
    }

    if (name.equals(RootClassLoader.class.getName())) {
      return RootClassLoader.class;
    } else if (name.equals(LauncherPlugin.class.getName())) {
      return LauncherPlugin.class;
    } else if (name.startsWith("net.labyfy.component.launcher.classloading.")) {
      return Class.forName(name, false, RootClassLoader.class.getClassLoader());
    }

    if (classCache.containsKey(name)) {
      return classCache.get(name);
    } else if (!transformEnabled || modificationExclusions.stream().anyMatch(name::startsWith)) {
      Class<?> clazz = super.findClass(name);
      classCache.put(name, clazz);
      return clazz;
    }

    currentlyLoading.add(name);

    try {
      CommonClassLoader loader = preferredLoader;
      ClassInformation information = null;

      if (loader != null) {
        information = CommonClassLoaderHelper.retrieveClass(loader, name);
      }

      for (Iterator<ChildClassLoader> it = children.iterator(); information == null && it.hasNext(); ) {
        loader = it.next();
        information = CommonClassLoaderHelper.retrieveClass(loader, name);
      }

      if (information == null) {
        loader = this;
        information = CommonClassLoaderHelper.retrieveClass(loader, name);

        if (information == null) {
          String finalName = name;
          logger.trace("Failed to find class {} after searching root and the following children: [{}]",
              () -> finalName,
              () -> children.stream()
                  .map(CommonClassLoader::getClassloaderName)
                  .collect(Collectors.joining(", ")));
          throw new ClassNotFoundException("Class " + name + " not found on classpath (searched root and "
              + children.size() + " child loaders)");
        }
      }

      byte[] classBytes = information.getClassBytes();

      for (LauncherPlugin plugin : plugins) {
        byte[] newBytes = plugin.modifyClass(name, classBytes);
        if (newBytes != null) {
          classBytes = newBytes;
        }
      }

      CodeSource codeSource = new CodeSource(information.getResourceURL(), information.getSigners());
      Class<?> clazz = loader.commonDefineClass(name, classBytes, 0, classBytes.length, codeSource);
      classCache.put(name, clazz);
      return clazz;
    } catch (IOException e) {
      throw new ClassNotFoundException("Failed to find class " + name + " due to IOException", e);
    } finally {
      currentlyLoading.remove(name);
    }
  }

  @Override
  public URL findResource(String name) {
    return findResource(name, true);
  }

  public URL findResource(String name, boolean allowRedirect) {
    try {
      Enumeration<URL> allWithName = findResources(name, allowRedirect);
      if (!allWithName.hasMoreElements()) {
        return null;
      }

      URL first = allWithName.nextElement();
      if (allWithName.hasMoreElements()) {
        byte[] firstData;
        if (resourceDataCache.containsKey(first)) {
          firstData = resourceDataCache.get(first);
        } else {
          firstData = CommonClassLoaderHelper.readResource(first.openConnection());
          resourceDataCache.put(first, firstData);
        }

        while (allWithName.hasMoreElements()) {
          URL next = allWithName.nextElement();
          byte[] nextData;

          if (resourceDataCache.containsKey(next)) {
            nextData = resourceDataCache.get(next);
          } else {
            nextData = CommonClassLoaderHelper.readResource(next.openConnection());
            resourceDataCache.put(next, nextData);
          }

          if (!Arrays.equals(firstData, nextData)) {
            // TODO: Currently, we have classpath conflicts, so this needs to be fixed first,
            //       then re-enable the throw!
            logger.warn("Resources with same name but different content found: ");
            logger.warn("\t{}", first.toExternalForm());
            logger.warn("\t{}", next.toExternalForm());
            // throw new UnsupportedOperationException("Resources with same name but different content found:\n" +
            //     "\t" + first.toExternalForm() +"\n\t" + next.toExternalForm());
          }
        }
      }

      return first;
    } catch (IOException e) {
      logger.warn("IOException while trying to retrieve resource " + name, e);
      return null;
    }
  }

  @Override
  public Enumeration<URL> findResources(String name) throws IOException {
    return findResources(name, true);
  }

  public Enumeration<URL> findResources(String name, boolean allowRedirect) throws IOException {
    List<URL> resources = Collections.list(super.findResources(name));
    for (ChildClassLoader childClassLoader : children) {
      resources.addAll(Collections.list(childClassLoader.commonFindResources(name)));
    }

    if (allowRedirect) {
      List<URL> adjustedResources = new ArrayList<>();
      for (URL suggested : resources) {
        for (LauncherPlugin plugin : plugins) {
          URL newSuggestion = plugin.adjustResourceURL(name, suggested);
          if (newSuggestion != null) {
            suggested = newSuggestion;
          }
        }

        adjustedResources.add(suggested);
      }

      return Collections.enumeration(adjustedResources);
    } else {
      return Collections.enumeration(resources);
    }
  }

  public Enumeration<URL> findAllResources() throws IOException {
    List<URL> collected = new ArrayList<>();

    for (URL url : getURLs()) {
      collected.addAll(CommonClassLoaderHelper.scanResources(url));
    }

    for (ChildClassLoader child : children) {
      collected.addAll(Collections.list(child.commonFindAllResources()));
    }

    return Collections.enumeration(collected);
  }

  public void registerChild(ChildClassLoader childClassloader) {
    if (!transformEnabled) {
      throw new IllegalStateException("ChildClassLoader's can only be registered after transformation has been enabled");
    } else if (children.contains(childClassloader)) {
      return;
    }

    children.add(childClassloader);
  }

  static {
    ClassLoader.registerAsParallelCapable();
  }

  @Override
  public Package commonDefinePackage(
      String name,
      String specTitle,
      String specVersion,
      String specVendor,
      String implTitle,
      String implVersion,
      String implVendor,
      URL sealBase
  ) {
    return definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
  }

  @Override
  public Package commonDefinePackage(String name, Manifest man, URL url) {
    return definePackage(name, man, url);
  }

  @Override
  public Class<?> commonDefineClass(String name, byte[] b, int off, int len, CodeSource cs) {
    return defineClass(name, b, off, len, cs);
  }

  @Override
  public URL commonFindResource(String name, boolean forClassLoad) {
    return findResource(name, !forClassLoad);
  }

  @Override
  public Enumeration<URL> commonFindResources(String name) throws IOException {
    return findResources(name);
  }

  @Override
  public Enumeration<URL> commonFindAllResources() throws IOException {
    return findAllResources();
  }

  @Override
  public Package commonGetPackage(String name) {
    return getPackage(name);
  }

  @Override
  public String getClassloaderName() {
    return "RootLoader";
  }
}
