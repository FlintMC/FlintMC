package net.labyfy.component.launcher.service;

import net.labyfy.component.launcher.classloading.RootClassLoader;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Service interface for plugins powering frameworks running on top of
 * applications launched by the {@link net.labyfy.component.launcher.LaunchController}.
 * <p>
 * Instances are loaded using a {@link java.util.ServiceLoader} or injected by other plugins.
 */
public interface LauncherPlugin {
  /**
   * The name of the plugin, should be a short, but meaningful
   * identifier, such as `Labyfy`, `Forge` or `Fabric`.
   *
   * @return the name of the plugin
   */
  String name();

    /**
     * Allows the plugin to modify the objects which arguments are
     * parsed to using JCommander.
     *
     * @param arguments the list of argument objects
     */
    default void adjustLoadCommandlineArguments(Set<Object> arguments) {
    }

  /**
   * Allows the plugin to inject other plugins into the plugin
   * loader.
   *
   * @return other plugins to load
   */
  default List<LauncherPlugin> extraPlugins() {
    return Collections.emptyList();
  }

    /**
     * Allows the plugin to modify the commandline arguments used for
     * launching and the next loading passes, if any
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
     * Gives the plugin a chance to execute code in the launch environment
     * before the launch is performed.
     *
     * @param launchClassloader The classloader used in the launch environment
     */
    default void preLaunch(ClassLoader launchClassloader) {
    }

  /**
   * Allows the plugin to modify classes before they are loaded.
   *
   * @param className the name of the class to modify
   * @param classData the class to modify
   * @return the modified data or null, if no modification was made
   */
  default byte[] modifyClass(String className, byte[] classData) {
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
