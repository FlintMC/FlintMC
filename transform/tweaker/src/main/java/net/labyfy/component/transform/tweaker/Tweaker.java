package net.labyfy.component.transform.tweaker;

import com.google.common.reflect.ClassPath;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Tweaker implements ITweaker {

  private final Map<String, String> launchArguments = new HashMap<>();

  public void injectIntoClassLoader(LaunchClassLoader launchClassLoader) {
    try {
      ClassPath.from(ClassLoader.getSystemClassLoader()).getTopLevelClassesRecursive("net.labyfy")
          .stream()
          .map(ClassPath.ClassInfo::load)
          .forEach(
              clazz -> {
                try {
                  System.out.println("Add url " + clazz);
                  launchClassLoader.addURL(
                      clazz
                          .getProtectionDomain()
                          .getCodeSource()
                          .getLocation()
                          .openConnection()
                          .getURL());
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });

      this.prepareLaunchClassLoader();
      launchClassLoader.registerTransformer(Transformer.class.getName());

      Class<?> aClass = launchClassLoader.findClass("net.labyfy.component.transform.tweaker.EntryPoint");
      System.out.println(aClass.getClassLoader());
      aClass.getDeclaredMethod("launch", Map.class).invoke(null, this.launchArguments);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void prepareLaunchClassLoader() throws IOException {}

  public void acceptOptions(List<String> arguments, File gameDir, File assetsDir, String version) {
    this.mapArguments(arguments);
    this.createDefault("--version", version);
    this.createDefault("--gameDir", gameDir.getPath());
    this.createDefault("--assetsDir", assetsDir.getPath());
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

  private void createDefault(String key, String value) {
    if (!this.launchArguments.containsKey(key)) this.launchArguments.put(key, value);
  }

  private void mapArguments(List<String> arguments) {
    for (int i = 0; i < arguments.size(); i += 2) {
      this.launchArguments.put(arguments.get(i), arguments.get(i + 1));
    }
  }
}
