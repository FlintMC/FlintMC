package net.labyfy.component.launcher;

import net.labyfy.component.launcher.classloading.RootClassloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LabyLauncher {
  public static void main(String[] args) throws Exception {
    RootClassloader rootClassloader = new RootClassloader(getClasspath().toArray(new URL[0]));
    Thread.currentThread().setContextClassLoader(rootClassloader);

    Class<?> launchControllerClass = rootClassloader.loadClass("net.labyfy.component.launcher.LaunchController");
    Object launchController = launchControllerClass.getDeclaredConstructors()[0].newInstance(rootClassloader, args);
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

    for(String classPathEntry : System.getProperty("java.class.path").split(pathSeparator)) {
      try {
        classPathUrls.add(Paths.get(classPathEntry).toUri().toURL());
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    }

    return classPathUrls;
  }
}
