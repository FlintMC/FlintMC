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

import com.beust.jcommander.JCommander;
import net.flintmc.launcher.classloading.RootClassLoader;
import net.flintmc.launcher.service.LauncherPlugin;
import net.flintmc.launcher.service.PreLaunchException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Main API system for the Launcher
 */
public class LaunchController {
  private static LaunchController instance;
  private final Logger logger;
  private final RootClassLoader rootLoader;
  private final List<String> commandLine;
  private final LaunchArguments launchArguments;

  /**
   * Constructs a new {@link LaunchController} instance and sets it as the active one.
   *
   * @param rootLoader  Class loader to use for loading
   * @param commandLine Commandline arguments to pass in
   * @throws IllegalStateException If a {@link LaunchController} instance has been created already
   */
  public LaunchController(RootClassLoader rootLoader, String[] commandLine) {
    if (instance != null) {
      throw new IllegalStateException(
          "The launcher cannot be instantiated twice in the same environment");
    }

    instance = this;
    this.rootLoader = rootLoader;
    this.logger = LogManager.getLogger(LaunchController.class);
    this.commandLine = new ArrayList<>(Arrays.asList(commandLine));
    this.launchArguments = new LaunchArguments();
  }

  /**
   * Retrieves the instance of the launcher the program has been launched with.
   *
   * @return Instance of the launcher or null if the program has not been launched with this
   * launcher
   */
  public static LaunchController getInstance() {
    return instance;
  }

  /**
   * Executes the launch. This is effectively the new `main` method.
   *
   * <p><b>Called by the {@link FlintLauncher} using reflection</b>
   */
  public void run() {
    logger.info("Initializing LaunchController");
    logger.info("Java version: {}", System.getProperty("java.version"));
    logger.info(
        "Operating System: {} {}", System.getProperty("os.name"), System.getProperty("os.version"));
    logger.info("JVM vendor: {}", System.getProperty("java.vendor"));


    File packages = new File("flint/packages");
    packages.mkdirs();
    for (File file : packages.listFiles()) {
      if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("jar")) {
        try {

          JarFile jarFile = new JarFile(file);
          Enumeration<JarEntry> entries = jarFile.entries();
          while (entries.hasMoreElements()){
            JarEntry jarEntry = entries.nextElement();
            if(!jarEntry.getName().equals("manifest.json"))continue;
            String manifest = IOUtils.toString(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8);
            System.out.println(manifest);
            System.out.println();
          }
          jarFile.close();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }


    // Find the first set of plugins by searching the classpath
    logger.trace("About to load plugins");
    ServiceLoader<LauncherPlugin> serviceLoader =
        ServiceLoader.load(LauncherPlugin.class, rootLoader);

    List<LauncherPlugin> plugins = new ArrayList<>();
    serviceLoader.forEach(plugins::add);
    logger.info(
        "Loaded {} initial {}.", plugins.size(), plugins.size() != 1 ? "plugins" : "plugin");
    rootLoader.addPlugins(plugins);

    int loadingPass = 0;
    List<LauncherPlugin> extraPlugins = new ArrayList<>();

    Set<Object> commandlineArguments = new HashSet<>();
    commandlineArguments.add(launchArguments);

    // Loading pass control flow:
    // 1. Let plugins register their commandline receivers
    // 2. Let plugins modify the current commandline for the first time
    // 3. Parse the commandline
    // -- From now on, the plugins may have been configured via the commandline
    // 4. Let plugins modify the current commandline for the second time
    // 5. Let plugins add extra plugins
    // 6. Copy extra plugins to main plugins if they are not in the main list already
    do {
      loadingPass++;
      extraPlugins.clear();
      logger.debug("Executing loading pass {}", loadingPass);

      // 1.
      plugins.forEach(plugin -> plugin.adjustLoadCommandlineArguments(commandlineArguments));

      // 2.
      plugins.forEach(plugin -> plugin.modifyCommandlineArguments(commandLine));

      // 3.
      JCommander commandlineParser = new JCommander();
      commandlineParser.addObject(commandlineArguments);
      commandlineParser.parse(commandLine.toArray(new String[0]));

      // 4.
      plugins.forEach(plugin -> plugin.modifyCommandlineArguments(commandLine));

      // 5.
      plugins.forEach(plugin -> extraPlugins.addAll(plugin.extraPlugins()));

      // 6.
      rootLoader.addPlugins(extraPlugins);
      logger.debug(
          "Found {} extra {}",
          extraPlugins.size(),
          extraPlugins.size() != 1 ? "plugins" : "plugin");
    } while (extraPlugins.size() > 0);

    // Registering and calling all plugins is done, continue with launch
    logger.info(
        "Took {} loading {} to initialize system, loaded {} {}",
        loadingPass,
        loadingPass != 1 ? "passes" : "pass",
        plugins.size(),
        plugins.size() != 1 ? "plugins" : "plugin");

    logger.trace(
        "Loaded plugins: {}",
        plugins.stream().map(LauncherPlugin::name).collect(Collectors.joining(", ")));

    // Prepare to hand control over to the launch target
    logger.info("Handing over to launch target {}", launchArguments.getLaunchTarget());
    rootLoader.prepare();

    LauncherPlugin exceptionContext = null;

    try {
      // Run final plugin operation within the new launch environment
      for (LauncherPlugin plugin : plugins) {
        exceptionContext = plugin;
        plugin.preLaunch(rootLoader);
      }

      Class<?> launchTarget = rootLoader.loadClass(launchArguments.getLaunchTarget());

      Method mainMethod = launchTarget.getMethod("main", String[].class);
      mainMethod.invoke(null, (Object) launchArguments.getOtherArguments().toArray(new String[0]));
    } catch (ClassNotFoundException exception) {
      logger.fatal("Failed to find launch target class", exception);
      System.exit(1);
    } catch (NoSuchMethodException exception) {
      logger.fatal("Launch target has no main method", exception);
      System.exit(1);
    } catch (InvocationTargetException exception) {
      logger.fatal("Invoking main method threw an error", exception);
      System.exit(1);
    } catch (IllegalAccessException exception) {
      logger.fatal("Unable to invoke main method due to missing access", exception);
      System.exit(1);
    } catch (PreLaunchException exception) {
      logger.fatal("Exception while invoking pre-launch callback: {}", exceptionContext, exception);
      System.exit(1);
    }
  }

  /**
   * Retrieves the class loader used by this launch controller for loading application classes
   *
   * @return Class loader used by this launch controller
   */
  public RootClassLoader getRootLoader() {
    return rootLoader;
  }
}
