package net.labyfy.component.transform.launchplugin;

import com.google.common.collect.*;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import net.labyfy.component.initializer.Initializer;
import net.labyfy.component.initializer.EntryPoint;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.launcher.classloading.RootClassLoader;
import net.labyfy.component.launcher.service.LauncherPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

public class LabyfyLauncherPlugin implements LauncherPlugin {
  private static LabyfyLauncherPlugin instance;

  public static LabyfyLauncherPlugin getInstance() {
    if (instance == null) {
      throw new IllegalStateException("LabyfyLauncherPlugin has not been instantiated yet");
    }

    return instance;
  }

  private final Logger logger;
  private final Multimap<Integer, LateInjectedTransformer> injectedTransformers;

  private List<String> launchArguments;

  public LabyfyLauncherPlugin() {
    if (instance != null) {
      throw new IllegalStateException("LabyfyLauncherPlugin instantiated already");
    }

    instance = this;

    this.logger = LogManager.getLogger(LabyfyLauncherPlugin.class);
    this.injectedTransformers = MultimapBuilder.treeKeys(Integer::compare).linkedListValues().build();
  }

  @Override
  public String name() {
    return "Labyfy";
  }

  @Override
  public void configureRootLoader(RootClassLoader classloader) {
    classloader.excludeFromModification("javassist.", "com.google.");
  }

  @Override
  public void modifyCommandlineArguments(List<String> arguments) {
    this.launchArguments = arguments;
  }

  @SuppressWarnings("InstantiationOfUtilityClass")
  @Override
  public void preLaunch(ClassLoader launchClassloader) {
    Map<String, String> arguments = new HashMap<>();

    for (Iterator<String> it = launchArguments.iterator(); it.hasNext(); ) {
      String key = it.next();
      if (it.hasNext()) {
        arguments.put(key, it.next());
      } else {
        arguments.put(key, null);
      }
    }

    new EntryPoint(arguments);

    try {
      Initializer.boot();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    InjectionHolder.enableIngameState();
  }

  @Override
  public byte[] modifyClass(String className, byte[] classData) {
    for (LateInjectedTransformer transformer : injectedTransformers.values()) {
      byte[] newData = transformer.transform(className, classData);
      if (newData != null) {
        classData = newData;
      }
    }

    try {
      CtClass ctClass =
          ClassPool.getDefault().makeClass(new ByteArrayInputStream(classData), false);

      CtConstructor initializer = ctClass.getClassInitializer();
      if (initializer == null) {
        initializer = ctClass.makeClassInitializer();
      }

      return ctClass.toBytecode();
    } catch (IOException e) {
      throw new RuntimeException("Failed to modify class due to IOException", e);
    } catch (CannotCompileException e) {
      logger.warn("Failed to modify class due to compilation error", e);
      return null;
    }
  }

  public void registerTransformer(int priority, LateInjectedTransformer transformer) {
    injectedTransformers.put(priority, transformer);
  }

}
