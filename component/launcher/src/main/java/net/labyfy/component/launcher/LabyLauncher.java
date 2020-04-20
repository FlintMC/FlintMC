package net.labyfy.component.launcher;

import com.beust.jcommander.JCommander;
import net.labyfy.component.launcher.classloading.RootClassloader;
import net.labyfy.component.launcher.service.LabyLauncherPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class LabyLauncher {
  private static LabyLauncher instance;

  public static LabyLauncher getInstance() {
    return instance;
  }

  public static void main(String[] args) {
    instance = new LabyLauncher(args);
    instance.run();
  }

  private final Logger logger;
  private final List<String> commandLine;
  private final LaunchArguments launchArguments;

  private LabyLauncher(String[] commandLine) {
    this.logger = LogManager.getLogger(LabyLauncher.class);
    this.commandLine = new ArrayList<>(Arrays.asList(commandLine));
    this.launchArguments = new LaunchArguments();
  }

  private RootClassloader rootLoader;

  private void run() {
    logger.info("Initializing LabyLauncher");
    logger.info("Java version: {}", System.getProperty("java.version"));
    logger.info("Operating System: {} {}", System.getProperty("os.name"), System.getProperty("os.version"));
    logger.info("JVM vendor: {}", System.getProperty("java.vendor"));

    // This is interesting, even tho there is File.pathSeparator, the JVM documentation
    // states that path.separator from the properties is used to separate the
    // java.class.path property. It currently is unknown, if they ever are different,
    // but to comply to the documentation make use of path.separator and fall back to
    // File.pathSeparator
    String pathSeparator = System.getProperty("path.separator", File.pathSeparator);

    List<URL> classPathUrls = new ArrayList<>();

    logger.debug("Converting class path to URL list...");
    for(String classPathEntry : System.getProperty("java.class.path").split(pathSeparator)) {
      try {
        logger.trace("Collecting {} ", classPathEntry);
        classPathUrls.add(Paths.get(classPathEntry).toUri().toURL());
      } catch (MalformedURLException e) {
        logger.warn("Failed to convert {} to an url, skipping", classPathEntry, e);
      }
    }

    logger.trace("About to load plugins");
    ServiceLoader<LabyLauncherPlugin> serviceLoader = ServiceLoader.load(LabyLauncherPlugin.class);

    List<LabyLauncherPlugin> plugins = new ArrayList<>();
    serviceLoader.forEach(plugins::add);
    logger.info("Loaded {} initial {}.", plugins.size(), plugins.size() != 1 ? "plugins" : "plugin");

    int loadingPass = 0;
    List<LabyLauncherPlugin> extraPlugins = new ArrayList<>();

    Set<Object> commandlineArguments = new HashSet<>();
    commandlineArguments.add(launchArguments);

    // Loading pass control flow:
    // 1. Let plugins register their commandline receivers
    // 2. Let plugins modify the current commandline for the first time
    // 3. Parse the commandline
    // -- From now on, the plugins may have been configured via the commandline
    // 4. Let plugins modify the current commandline for the second time
    // 5. Let plugins modify the classpath
    // 6. Let plugins add extra plugins
    // 7. Copy extra plugins to main plugins if they are not in the main list already
    // 8. If extra plugins were added, repeat the flow
    do {
      loadingPass++;
      extraPlugins.clear();
      logger.debug("Executing loading pass {}", loadingPass);

      plugins.forEach(plugin -> plugin.adjustLoadCommandlineArguments(commandlineArguments));
      plugins.forEach(plugin -> plugin.modifyCommandlineArguments(commandLine));

      JCommander commandlineParser = new JCommander();
      commandlineParser.addObject(commandlineArguments);

      commandlineParser.parse(commandLine.toArray(new String[0]));
      plugins.forEach(plugin -> plugin.modifyCommandlineArguments(commandLine));

      plugins.forEach(plugin -> plugin.adjustClasspath(classPathUrls));
      plugins.forEach(plugin -> extraPlugins.addAll(plugin.extraPlugins()));

      logger.debug("Found {} extra {}", extraPlugins.size(), extraPlugins.size() != 1 ? "plugins" : "plugin");
    } while (extraPlugins.size() > 0);

    logger.info(
        "Took {} loading {} to initialize system, loaded {} {}",
        loadingPass,
        loadingPass != 1 ? "passes" : "pass",
        plugins.size(),
        plugins.size() != 1 ? "plugins" : "plugin"
    );

    logger.trace("Loaded plugins: {}",
        plugins.stream().map(LabyLauncherPlugin::name).collect(Collectors.joining(", ")));

    logger.info("Handing over to launch target {}", launchArguments.getLaunchTarget());
    rootLoader = new RootClassloader(classPathUrls.toArray(new URL[0]), plugins);
    rootLoader.prepare();

    try {
      Thread.currentThread().setContextClassLoader(rootLoader);
      plugins.forEach(plugin -> plugin.preLaunch(rootLoader));
      Class<?> launchTarget = rootLoader.loadClass(launchArguments.getLaunchTarget());

      Method mainMethod = launchTarget.getMethod("main", String[].class);
      mainMethod.invoke(null, (Object) launchArguments.getOtherArguments().toArray(new String[0]));
    } catch (ClassNotFoundException e) {
      logger.fatal("Failed to find launch target class", e);
      System.exit(1);
    } catch (NoSuchMethodException e) {
      logger.fatal("Launch target has no main method", e);
      System.exit(1);
    } catch (InvocationTargetException e) {
      logger.fatal("Invoking main method threw an error", e);
      System.exit(1);
    } catch (IllegalAccessException e) {
      logger.fatal("Unable to invoke main method due to missing access", e);
      System.exit(1);
    }
  }

  public RootClassloader getRootLoader() {
    return rootLoader;
  }
}
