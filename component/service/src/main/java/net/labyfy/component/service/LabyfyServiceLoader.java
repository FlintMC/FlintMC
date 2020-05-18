package net.labyfy.component.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LabyfyServiceLoader<T> {
  private static final Map<Class<?>, LabyfyServiceLoader<?>> loaders = new ConcurrentHashMap<>();

  @SuppressWarnings("unchecked")
  public static <T> LabyfyServiceLoader<T> get(Class<T> targetClass) {
    if(loaders.containsKey(targetClass)) {
      return (LabyfyServiceLoader<T>) loaders.get(targetClass);
    }

    LabyfyServiceLoader<T> loader = new LabyfyServiceLoader<>(targetClass);
    loaders.put(targetClass, loader);
    return loader;
  }

  private final Class<T> targetClass;
  private final List<String> alreadyDiscovered;

  private LabyfyServiceLoader(Class<T> targetClass) {
    this.targetClass = targetClass;
    this.alreadyDiscovered = new ArrayList<>();
  }

  public Set<T> discover(ClassLoader loader) {
    Enumeration<URL> serviceFiles;
    try {
      serviceFiles = loader.getResources("META-INF/services/" + targetClass.getName());
    } catch (IOException e) {
      throw new ServiceLoadException("Failed to discover services due to IOException while querying ClassLoader", e);
    }

    Set<Class<? extends T>> collectedServiceClasses = new HashSet<>();

    while(serviceFiles.hasMoreElements()) {
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

    try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.openStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if(line.startsWith("#") || line.isEmpty() || alreadyDiscovered.contains(line)) {
          continue;
        }

        alreadyDiscovered.add(line);

        Class<?> loaded = loader.loadClass(line);
        if(!targetClass.isAssignableFrom(loaded)) {
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
