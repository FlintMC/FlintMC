package net.labyfy.component.transform.tweaker;

import com.google.common.reflect.ClassPath;
import net.labyfy.base.structure.service.Service;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class LabyDebugTweaker implements ITweaker {

  private final Map<String, String> launchArguments = new HashMap<>();

  public void injectIntoClassLoader(LaunchClassLoader launchClassLoader) {
    try {
      Set<? extends Class<?>> collect =
          ClassPath.from(ClassLoader.getSystemClassLoader()).getAllClasses().stream()
              .filter(classInfo -> classInfo.getName().startsWith("net.labyfy"))
              .map(ClassPath.ClassInfo::load)
              .collect(Collectors.toSet());

      launchClassLoader.registerTransformer(
          "net.labyfy.component.transform.tweaker.LabyTransformer");

      launchClassLoader
          .loadClass("net.labyfy.component.initializer.EntryPoint")
          .getDeclaredConstructors()[0]
          .newInstance(this.launchArguments);

      Collection<Class> classes = new HashSet<>();
      collect.forEach(
          clazz -> {
            try {

              if (clazz.isAnnotationPresent(Service.class)) {
                Class.forName(clazz.getName(), true, launchClassLoader);
              } else {
                classes.add(clazz);
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          });

      for (Class clazz : classes) {
        Class.forName(clazz.getName(), true, launchClassLoader);
      }
    } catch (IOException
        | ClassNotFoundException
        | IllegalAccessException
        | InstantiationException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public void acceptOptions(List<String> arguments, File gameDir, File assetsDir, String version) {
    this.mapArguments(arguments);
    this.createDefaults(version, gameDir, assetsDir);
  }

  public String[] getLaunchArguments() {
    Collection<String> argumentList = (Collection<String>) Launch.blackboard.get("ArgumentList");
    if (argumentList.isEmpty()) {
      argumentList.addAll(
          this.launchArguments.entrySet().stream()
              .map(entry -> new String[] {entry.getKey(), entry.getValue()})
              .flatMap(Arrays::stream)
              .collect(Collectors.toList()));
    }

    return new String[] {};
  }

  public String getLaunchTarget() {
    return "net.minecraft.client.main.Main";
  }

  private void createDefaults(String version, File gameDir, File assetsDir) {
    this.createDefault("--version", version);
    this.createDefault("--gameDir", gameDir.getPath());
    this.createDefault("--assetsDir", assetsDir.getPath());
  }

  private void createDefault(String key, String value) {
    if (!this.launchArguments.containsKey(key)) this.launchArguments.put(key, value);
  }

  private void mapArguments(List<String> arguments) {
    for (int i = 0; i < arguments.size(); i += 2) {
      this.launchArguments.put(arguments.get(i), arguments.get(i + 1));
    }
  }
}
