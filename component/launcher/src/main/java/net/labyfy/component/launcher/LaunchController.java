package net.labyfy.component.launcher;

import com.beust.jcommander.JCommander;
import io.sentry.Sentry;
import io.sentry.SentryOptions;
import net.labyfy.component.launcher.classloading.RootClassLoader;
import net.labyfy.component.launcher.service.LauncherPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

public class LaunchController {
  private static LaunchController instance;

  public static LaunchController getInstance() {
    return instance;
  }

  private final Logger logger;
  private final RootClassLoader rootLoader;
  private final List<String> commandLine;
  private final LaunchArguments launchArguments;

  public LaunchController(RootClassLoader rootLoader, String[] commandLine) {
    instance = this;
    this.rootLoader = rootLoader;
    this.logger = LogManager.getLogger(LaunchController.class);
    this.commandLine = new ArrayList<>(Arrays.asList(commandLine));
    this.launchArguments = new LaunchArguments();
  }

  public void run() {
    logger.info("Initializing LaunchController");
    logger.info("Java version: {}", System.getProperty("java.version"));
    logger.info("Operating System: {} {}", System.getProperty("os.name"), System.getProperty("os.version"));
    logger.info("JVM vendor: {}", System.getProperty("java.vendor"));

    logger.info("Initializing Sentry");
    Sentry.init("https://413b8f3fc06b407f9e8b1f5bd41258eb@sentry.labymod.net/2?release=" + findVersionInfo());

    logger.trace("About to load plugins");
    ServiceLoader<LauncherPlugin> serviceLoader = ServiceLoader.load(LauncherPlugin.class, rootLoader);

    List<LauncherPlugin> plugins = new ArrayList<>();
    serviceLoader.forEach(plugins::add);
    logger.info("Loaded {} initial {}.", plugins.size(), plugins.size() != 1 ? "plugins" : "plugin");
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
    // 5. Let plugins modify the classpath
    // 6. Let plugins add extra plugins
    // 7. Copy extra plugins to main plugins if they are not in the main list already
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
      plugins.forEach(plugin -> extraPlugins.addAll(plugin.extraPlugins()));

      rootLoader.addPlugins(extraPlugins);
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
        plugins.stream().map(LauncherPlugin::name).collect(Collectors.joining(", ")));

    logger.info("Handing over to launch target {}", launchArguments.getLaunchTarget());
    rootLoader.prepare();

    try {
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

  public RootClassLoader getRootLoader() {
    return rootLoader;
  }

  private String findVersionInfo() {
    try {
      Enumeration<URL> resources = Thread.currentThread().getContextClassLoader()
              .getResources("META-INF/MANIFEST.MF");
      while (resources.hasMoreElements()) {
        URL manifestUrl = resources.nextElement();
        Manifest manifest = new Manifest(manifestUrl.openStream());
        Attributes mainAttributes = manifest.getMainAttributes();
        String implementationTitle = mainAttributes.getValue("Implementation-Title");
        if (implementationTitle != null && implementationTitle.equals("labyfy")) {
          return mainAttributes.getValue("Implementation-Version");
        }
      }
    } catch (Exception e){
      logger.error(e);
    }
    return "unknown";
  }
}
