package net.flintmc.launcher;

import net.flintmc.launcher.classloading.RootClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FlintLauncher {
  public static void main(String[] args) throws Exception {
    // Root entry point
    // Switch to the RootClassLoader as soon as possible
    RootClassLoader rootClassloader = new RootClassLoader(getClasspath().toArray(new URL[0]));
    Thread.currentThread().setContextClassLoader(rootClassloader);

    // We now are running in the RootClassLoader's context, load our LaunchController
    Class<?> launchControllerClass =
        rootClassloader.loadClass("net.flintmc.launcher.LaunchController");
    Object launchController =
        launchControllerClass.getDeclaredConstructors()[0].newInstance(rootClassloader, args);

    // Hand over to the LaunchController
    launchControllerClass.getMethod("run").invoke(launchController);
  }

  private static List<URL> getClasspath() {
    // This is interesting, even tho there is File.pathSeparator, the JVM documentation
    // states that path.separator from the properties is used to separate the
    // java.class.path property. It currently is unknown, if they ever are different,
    // but to comply to the documentation make use of path.separator and fall back to
    // File.pathSeparator
    String pathSeparator = System.getProperty("path.separator", File.pathSeparator);

    List<URL> classPathUrls = new ArrayList<>();

    for (String classPathEntry : System.getProperty("java.class.path").split(pathSeparator)) {
      try {
        classPathUrls.add(Paths.get(classPathEntry).toUri().toURL());
      } catch (MalformedURLException e) {
        // This is fine... Nothing we can really do here except logging the
        // exception and hoping for the best
        e.printStackTrace();
      }
    }

    return classPathUrls;
  }
}
