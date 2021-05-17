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

package net.flintmc.transform.launchplugin;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.NotFoundException;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.launcher.LaunchController;
import net.flintmc.launcher.classloading.ClassTransformException;
import net.flintmc.launcher.classloading.RootClassLoader;
import net.flintmc.launcher.classloading.common.CommonClassLoader;
import net.flintmc.launcher.service.LauncherPlugin;
import net.flintmc.launcher.service.PreLaunchException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class FlintLauncherPlugin implements LauncherPlugin {

  private static FlintLauncherPlugin instance;
  private final Logger logger;
  private final Multimap<Integer, LateInjectedTransformer> injectedTransformers;
  private List<String> launchArguments;

  public FlintLauncherPlugin() {
    if (instance != null) {
      throw new IllegalStateException("FlintLauncherPlugin instantiated already");
    }
    RootClassLoader rootLoader = LaunchController.getInstance().getRootLoader();

    ClassPool.getDefault()
        .appendClassPath(
            new ClassPath() {
              public InputStream openClassfile(String classname) throws NotFoundException {
                URL result = find(classname);
                if (result == null) {
                  throw new NotFoundException("Class " + classname + " not found");
                }

                try {
                  return result.openStream();
                } catch (IOException exception) {
                  throw new NotFoundException("Failed to open class " + classname, exception);
                }
              }

              public URL find(String classname) {
                return rootLoader.getResource(classname.replace('.', '/') + ".class");
              }
            });

    instance = this;

    this.logger = LogManager.getLogger(FlintLauncherPlugin.class);
    this.injectedTransformers =
        MultimapBuilder.treeKeys(Integer::compare).linkedListValues().build();
  }

  public static FlintLauncherPlugin getInstance() {
    if (instance == null) {
      throw new IllegalStateException("FlintLauncherPlugin has not been instantiated yet");
    }
    return instance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String name() {
    return "Flint";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void configureRootLoader(RootClassLoader classloader) {
    classloader.excludeFromModification(
        "javassist.", "com.google.inject.", "com.google.common.", "net.flintmc.transform.");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void modifyCommandlineArguments(List<String> arguments) {
    this.launchArguments = arguments;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void preLaunch(ClassLoader launchClassloader) throws PreLaunchException {
    Map<String, String> arguments = new HashMap<>();

    // Collect minecraft launch arguments as a map
    for (Iterator<String> it = launchArguments.iterator(); it.hasNext(); ) {
      String key = it.next();
      if (it.hasNext()) {
        arguments.put(key, it.next());
      } else {
        arguments.put(key, null);
      }
    }

    InjectionHolder.getInjectedInstance(FlintFrameworkInitializer.class).initialize(arguments);
  }

  public void registerTransformer(int priority, LateInjectedTransformer transformer) {
    injectedTransformers.put(priority, transformer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] modifyClass(String className, CommonClassLoader classLoader, byte[] classData)
      throws ClassTransformException {
    for (LateInjectedTransformer transformer : injectedTransformers.values()) {
      byte[] newData = transformer.transform(className, classLoader, classData);
      if (newData != null) {
        for (LateInjectedTransformer notifyTransformer : injectedTransformers.values()) {
          notifyTransformer.notifyTransform(transformer, className, newData);
        }
        classData = newData;
      }
    }
    return classData;
  }

  /**
   * Search for manifest.mf entries for Flint
   *
   * @param name Name of the requested entry
   * @return Value for requested entry
   */
  private String findManifestEntry(String name) throws IOException {
    Enumeration<URL> resources =
        Thread.currentThread().getContextClassLoader().getResources("META-INF/MANIFEST.MF");
    while (resources.hasMoreElements()) {
      URL manifestUrl = resources.nextElement();
      Manifest manifest = new Manifest(manifestUrl.openStream());
      Attributes mainAttributes = manifest.getMainAttributes();
      String implementationTitle = mainAttributes.getValue("Implementation-Title");
      if (implementationTitle != null && implementationTitle.equals("flint")) {
        return mainAttributes.getValue(name);
      }
    }
    return null;
  }
}
