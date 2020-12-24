/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.framework.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Extends the functionality of the default java {@link ServiceLoader}. The difference to the java
 * {@link ServiceLoader} is the reload and multi classloader capability.
 *
 * <p>example:
 *
 * <blockquote>
 *
 * <pre>
 *
 * package com.example;
 *
 * interface Service {
 *      void doSomething();
 * }
 *
 * class ServiceImpl implements Service {
 *    {@literal @}Override
 *     public void doSomething() {}
 * }
 * </pre>
 *
 * </blockquote>
 *
 * <p>Content of "META-INF/services/com.example.Service": {@code com.example.ServiceImpl}
 *
 * <p>To find all implementations: {@code Set<Service> services =
 * ExtendedServiceLoader.get(Service.class).discover(classLoader);}
 *
 * @see ServiceLoader
 */
public class ExtendedServiceLoader<T> {
  private static final Map<Class<?>, ExtendedServiceLoader<?>> loaders = new ConcurrentHashMap<>();
  private final Class<T> targetClass;
  private final List<String> alreadyDiscovered;

  private ExtendedServiceLoader(Class<T> targetClass) {
    this.targetClass = targetClass;
    this.alreadyDiscovered = new ArrayList<>();
  }

  /**
   * Reads the content of all resources with the name of "META-INF/services/targetClass#getName()"
   * (can be multiple files), and collects all service implementations of those files.
   *
   * @param targetClass service class to load
   * @param <T> type of service to load
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
    } catch (IOException exception) {
      throw new ServiceLoadException(
          "Failed to discover services due to IOException while querying ClassLoader", exception);
    }

    Set<Class<? extends T>> collectedServiceClasses = new HashSet<>();

    while (serviceFiles.hasMoreElements()) {
      URL serviceFile = serviceFiles.nextElement();
      collectedServiceClasses.addAll(parseAndLoadServiceFile(serviceFile, loader));
    }

    Set<T> instances = new HashSet<>();
    collectedServiceClasses.forEach(
        (clazz) -> {
          try {
            instances.add(clazz.newInstance());
          } catch (InstantiationException exception) {
            throw new ServiceLoadException("Failed to instantiate " + clazz.getName(), exception);
          } catch (IllegalAccessException exception) {
            throw new ServiceLoadException(
                "Failed to instantiate " + clazz.getName() + " due to missing access rights",
                exception);
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
          throw new ServiceLoadException(
              "Service file "
                  + file.toExternalForm()
                  + " mentioned class "
                  + loaded.getName()
                  + ", but it is not assignable to "
                  + targetClass.getName());
        }

        classes.add((Class<? extends T>) loaded);
      }
    } catch (IOException exception) {
      throw new ServiceLoadException(
          "Failed to read service file " + file.toExternalForm() + " due to IOException",
          exception);
    } catch (ClassNotFoundException exception) {
      throw new ServiceLoadException(
          "Service file " + file.toExternalForm() + " mentioned a nonexistent class", exception);
    }

    return classes;
  }
}
