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
import java.util.jar.Manifest;
import java.util.stream.Collectors;

public class RootClassLoader extends URLClassLoader implements CommonClassLoader {
  private final Set<String> currentlyLoading;
  private final Set<LauncherPlugin> plugins;
  private final List<ChildClassLoader> children;
  private final List<String> modificationExclusions;
  private final Map<String, Class<?>> classCache;
  private final Logger logger;

  private boolean transformEnabled;

  public RootClassLoader(URL[] urls) {
    super(urls, null);
    this.currentlyLoading = new HashSet<>();
    this.plugins = new HashSet<>();
    this.children = new ArrayList<>();
    this.modificationExclusions = new ArrayList<>();
    this.classCache = new WeakHashMap<>();
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
    if(currentlyLoading.contains(name)) {
      throw new IllegalStateException("Circular load detected: " + name);
    }

    if(name.equals(RootClassLoader.class.getName())) {
      return RootClassLoader.class;
    } else if(name.equals(LauncherPlugin.class.getName())) {
      return LauncherPlugin.class;
    } else if(name.startsWith("net.labyfy.component.launcher.classloading.")) {
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

      if(loader != null) {
        information = CommonClassLoaderHelper.retrieveClass(loader, name);
      }

      for (Iterator<ChildClassLoader> it = children.iterator(); information == null && it.hasNext();) {
        loader = it.next();
        information = CommonClassLoaderHelper.retrieveClass(loader, name);
      }

      if(information == null) {
        loader = this;
        information = CommonClassLoaderHelper.retrieveClass(loader, name);

        if(information == null) {
          logger.trace("Failed to find class {} after searching root and the following children: [{}]",
              () -> name,
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
      throw new ClassNotFoundException("Failed to find class " + name + " due to IOException");
    } finally {
      currentlyLoading.remove(name);
    }
  }

  @Override
  public URL findResource(String name) {
    return findResource(name, true);
  }

  public URL findResource(String name, boolean allowRedirect) {
    if (allowRedirect) {
      URL suggested = super.findResource(name);
      for (LauncherPlugin plugin : plugins) {
        URL newSuggestion = plugin.adjustResourceURL(name, suggested);
        if (newSuggestion != null) {
          suggested = newSuggestion;
        }
      }

      return suggested;
    }

    return super.findResource(name);
  }

  public void registerChild(ChildClassLoader childClassloader) {
    if(!transformEnabled) {
      throw new IllegalStateException("ChildClassLoader's can only be registered after transformation has been enabled");
    } else if(children.contains(childClassloader)) {
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
  public Package commonGetPackage(String name) {
    return getPackage(name);
  }

  @Override
  public String getClassloaderName() {
    return "RootLoader";
  }
}
