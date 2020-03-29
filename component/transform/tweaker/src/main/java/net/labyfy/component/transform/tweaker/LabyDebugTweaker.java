package net.labyfy.component.transform.tweaker;

import com.google.common.reflect.ClassPath;
import com.google.inject.Injector;
import net.labyfy.base.structure.Initialize;
import net.labyfy.base.structure.Initializer;
import net.labyfy.base.structure.service.Service;
import net.labyfy.component.initializer.inject.InitializationModule;
import net.labyfy.component.initializer.inject.module.BindConstantModule;
import net.labyfy.component.initializer.inject.module.PostConstructModule;
import net.labyfy.component.inject.InjectionHolder;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class LabyDebugTweaker implements ITweaker {

  private final Map<String, String> launchArguments = new HashMap<>();

  public void injectIntoClassLoader(LaunchClassLoader launchClassLoader) {
    try {
      Launch.classLoader.addTransformerExclusion("javassist");
      Launch.classLoader.addTransformerExclusion("com.google.inject");

      launchClassLoader.registerTransformer(
          "net.labyfy.component.transform.tweaker.LabyTransformer");

      launchClassLoader
          .loadClass("net.labyfy.component.initializer.EntryPoint")
          .getDeclaredConstructors()[0]
          .newInstance(this.launchArguments);


      Class<?> initializerClass = Launch.classLoader.loadClass(Initializer.class.getName());
      Method boot = initializerClass.getDeclaredMethod("boot");
      boot.invoke(null);


            Class<?> injectionHolder =
       Launch.classLoader.loadClass(InjectionHolder.class.getName());
            Method enableIngameState = injectionHolder.getDeclaredMethod("enableIngameState");
            enableIngameState.invoke(null);

    } catch (ClassNotFoundException
        | IllegalAccessException
        | InstantiationException
        | InvocationTargetException
        | NoSuchMethodException e) {
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
