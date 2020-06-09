package net.labyfy.gradle.library; // Created by leo on 25.09.19

import javax.inject.Singleton;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class ReflectionSuperClassProvider implements SuperClassProvider {

  private URLClassLoader classLoader;
  private Method addURLMethod;

  protected ReflectionSuperClassProvider(File jarFile, Collection<File> libraries)
      throws NoSuchMethodException, MalformedURLException, InvocationTargetException,
      IllegalAccessException {
    this.classLoader = new URLClassLoader(new URL[]{});
    this.addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
    this.addURLMethod.setAccessible(true);
    this.addURL(jarFile.toURI().toURL());
    libraries.stream()
        .map(File::toPath)
        .filter(path -> (path.toFile().isFile() && path.toFile().getName().endsWith(".jar")))
        .forEach(
            path -> {
              File file = path.toFile();
              try {
                addURL(file.toURI().toURL());
              } catch (InvocationTargetException
                  | IllegalAccessException
                  | MalformedURLException e) {
                e.printStackTrace();
              }
            });
  }

  private void addURL(URL url) throws InvocationTargetException, IllegalAccessException {
    this.addURLMethod.invoke(classLoader, url);
  }

  public List<String> getSuperClass(String clazz) {
    try {
      Class theClazz = this.classLoader.loadClass(clazz);
      Class superClazz = theClazz.getSuperclass();
      ArrayList<String> classes = new ArrayList<>();
      if (superClazz != null) classes.add(superClazz.getName());
      if (theClazz.getInterfaces() != null) {
        for (Class iface : theClazz.getInterfaces()) {
          classes.add(iface.getName());
        }
      }

      ArrayList<String> transitiveSuperClasses = new ArrayList<>();
      classes.forEach(c -> transitiveSuperClasses.addAll(getSuperClass(c)));
      classes.addAll(transitiveSuperClasses);
      return classes;
    } catch (Throwable ignored) {
      //Not found, can be ignored
      return null;
    }
  }
}
