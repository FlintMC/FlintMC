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

package net.flintmc.launcher.service;

import net.flintmc.launcher.LaunchController;
import net.flintmc.launcher.classloading.RootClassLoader;
import net.flintmc.launcher.classloading.common.CommonClassLoader;
import net.flintmc.transform.exceptions.ClassTransformException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Service interface for plugins powering frameworks running on top of applications launched by the
 * {@link LaunchController}.
 *
 * <p>Instances are loaded using a {@link java.util.ServiceLoader} or injected by other plugins.
 */
public interface LauncherPlugin {

  /**
   * The name of the plugin, should be a short, but meaningful identifier, such as `Flint`, `Forge`
   * or `Fabric`.
   *
   * @return the name of the plugin
   */
  String name();

  /**
   * Allows the plugin to modify the objects which arguments are parsed to using JCommander.
   *
   * @param arguments the list of argument objects
   */
  default void adjustLoadCommandlineArguments(Set<Object> arguments) {
  }

  /**
   * Allows the plugin to inject other plugins into the plugin loader.
   *
   * @return other plugins to load
   */
  default List<LauncherPlugin> extraPlugins() {
    return Collections.emptyList();
  }

  /**
   * Allows the plugin to modify the commandline arguments used for launching and the next loading
   * passes, if any
   *
   * @param arguments the current commandline arguments
   */
  default void modifyCommandlineArguments(List<String> arguments) {
  }

  /**
   * Allows the plugin to modify the behavior of the root classloader.
   *
   * @param classloader the root classloader used for classloading from now on
   */
  default void configureRootLoader(RootClassLoader classloader) {
  }

  /**
   * Gives the plugin a chance to execute code in the launch environment before the launch is
   * performed.
   *
   * @param launchClassloader The classloader used in the launch environment
   * @throws PreLaunchException If the pre-launch failed.
   */
  default void preLaunch(ClassLoader launchClassloader) throws PreLaunchException {
  }

  /**
   * Allows the plugin to modify classes before they are loaded.
   *
   * @param className   The non-null name of the class to modify
   * @param classLoader The non-null class loader that will be used to define the modified class
   * @param classData   The class to modify
   * @return the modified data or null, if no modification was made
   * @throws ClassTransformException If the class fails to transform
   */
  default byte[] modifyClass(String className, CommonClassLoader classLoader, byte[] classData)
      throws ClassTransformException {
    return null;
  }

  /**
   * Allows the plugin to override where resources can be found.
   *
   * @param resourceName the name of the resource to be found
   * @param suggested    the currently suggested url, may be null
   * @return the adjusted url or null to indicate no change
   */
  default URL adjustResourceURL(String resourceName, URL suggested) {
    return null;
  }
}
