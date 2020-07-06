package net.labyfy.component.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Extends the functionality of the default java {@link ServiceLoader}.
 * The difference to the java {@link ServiceLoader} is the reload and multi classloader capability.
 * <p>
 * example:
 * <pre>
 * {@code
 * package com.example;
 * interface Service{
 *
 * void doSomething();
 * }
 *
 * class ServiceImpl implements Service{
 *
 * @Override
 * public void doSomething(){
 * }
 * }
 * }
 * </pre>
 * <p>
 * Content of "META-INF/services/com.example.Service":
 * <p>
 * To find all implementations:
 * Set<Service> services = ExtendedServiceLoader.get(Service.class).discover(classLoader);
 *
 * @see ServiceLoader
 */
public class ExtendedServiceLoader<T> {
  private static final Map<Class<?>, ExtendedServiceLoader<?>> loaders = new ConcurrentHashMap<>();

  private ExtendedServiceLoader(Class<T> targetClass) {
    this.targetClass = targetClass;
    this.alreadyDiscovered = new ArrayList<>();
  }

  private final Class<T> targetClass;
  private final List<String> alreadyDiscovered;

  /**
   * Reads the content of all resources with the name of "META-INF/services/targetClass#getName()" (can be multiple files),
   * and collects all service implementations of those files.
   *
   * @param targetClass service class to load
   * @param <T>         type of service to load
   * @return ExtendedServiceLoader configured to find services of type T
   */
  @SuppressWarnings("unchecked")
  public static <T> ExtendedServiceLoader<T> get(Class<T> targetClass) {
    if (loaders.containsKey(targetClass)) {
      return (ExtendedServiceLoader<T>) loaders.get(targetClass);
    }

    ExtendedServiceLoader<T> loader = new ExtendedServiceLoader<>(targetClass);
    loaders.put(targetClass, loader);
    return loader;
  }

  /**
   * Finds all service files, interpretes them and instantiates the found classes
   *
   * @param loader
   * @return
   */
  public Set<T> discover(ClassLoader loader) {
    Enumeration<URL> serviceFiles;
    try {
      serviceFiles = loader.getResources("META-INF/services/" + targetClass.getName());
    } catch (IOException e) {
      throw new ServiceLoadException("Failed to discover services due to IOException while querying ClassLoader", e);
    }

    Set<Class<? extends T>> collectedServiceClasses = new HashSet<>();

    while (serviceFiles.hasMoreElements()) {
      URL serviceFile = serviceFiles.nextElement();
      collectedServiceClasses.addAll(parseAndLoadServiceFile(serviceFile, loader));
    }

    Set<T> instances = new HashSet<>();
    collectedServiceClasses.forEach((clazz) -> {
      try {
        instances.add(clazz.newInstance());
      } catch (InstantiationException e) {
        throw new ServiceLoadException("Failed to instantiate " + clazz.getName());
      } catch (IllegalAccessException e) {
        throw new ServiceLoadException("Failed to instantiate " + clazz.getName() + " due to missing access rights");
      }
    });

    return instances;
  }

  @SuppressWarnings("unchecked")
  private Set<Class<? extends T>> parseAndLoadServiceFile(URL file, ClassLoader loader) {
    Set<Class<? extends T>> classes = new HashSet<>();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.openStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.startsWith("#") || line.isEmpty() || alreadyDiscovered.contains(line)) {
          continue;
        }

        alreadyDiscovered.add(line);

        Class<?> loaded = loader.loadClass(line);
        if (!targetClass.isAssignableFrom(loaded)) {
          throw new ServiceLoadException("Service file " + file.toExternalForm() + " mentioned class " +
              loaded.getName() + ", but it is not assignable to " + targetClass.getName());
        }

        classes.add((Class<? extends T>) loaded);
      }
    } catch (IOException e) {
      throw new ServiceLoadException("Failed to read service file " + file.toExternalForm() + " due to IOException", e);
    } catch (ClassNotFoundException e) {
      throw new ServiceLoadException("Service file " + file.toExternalForm() + " mentioned a nonexistent class");
    }

    return classes;
  }
}
