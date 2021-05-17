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

package net.flintmc.launcher;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.flintmc.launcher.classloading.RootClassLoader;
import sun.misc.Unsafe;

public class FlintLauncher {

  public static void main(String[] args) throws Exception {
    main(args, new URL[]{});
  }


  public static void main(String[] args, URL[] urls) throws Exception {
    // Root entry point
    // Switch to the RootClassLoader as soon as possible
    List<URL> classpath = getClasspath();
    classpath.addAll(Arrays.asList(urls));

    RootClassLoader rootClassloader = new RootClassLoader(classpath.toArray(new URL[]{}));
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
