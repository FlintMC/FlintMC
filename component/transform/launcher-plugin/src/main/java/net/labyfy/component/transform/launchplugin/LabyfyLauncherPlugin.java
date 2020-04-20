package net.labyfy.component.transform.launchplugin;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import net.labyfy.base.structure.Initializer;
import net.labyfy.component.initializer.EntryPoint;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.launcher.classloading.RootClassloader;
import net.labyfy.component.launcher.service.LauncherPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

public class LabyfyLauncherPlugin implements LauncherPlugin {
  private static LabyfyLauncherPlugin instance;

  public static LabyfyLauncherPlugin getInstance() {
    if(instance == null) {
      throw new IllegalStateException("LabyfyLauncherPlugin has not been instantiated yet");
    }

    return instance;
  }

  private final Logger logger;
  private final List<LateInjectedTransformer> injectedTransformers;

  private List<String> launchArguments;

  public LabyfyLauncherPlugin() {
    if(instance != null) {
      throw new IllegalStateException("LabyfyLauncherPlugin instantiated already");
    }

    instance = this;

    this.logger = LogManager.getLogger(LabyfyLauncherPlugin.class);
    this.injectedTransformers = new ArrayList<>();
  }

  @Override
  public String name() {
    return "Labyfy";
  }

  @Override
  public void configureRootLoader(RootClassloader classloader) {
    classloader.excludeFromModification(
        "javassist.",
        "com.google.inject."
    );
  }

  @Override
  public void modifyCommandlineArguments(List<String> arguments) {
    this.launchArguments = arguments;
  }

  @SuppressWarnings("InstantiationOfUtilityClass")
  @Override
  public void preLaunch(ClassLoader launchClassloader) {
    Map<String, String> arguments = new HashMap<>();

    for(Iterator<String> it = launchArguments.iterator(); it.hasNext();) {
      String key = it.next();
      if(it.hasNext()) {
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
    for(LateInjectedTransformer transformer : injectedTransformers) {
      byte[] newData = transformer.transform(className, classData);
      if(newData != null) {
        classData = newData;
      }
    }


    try {
      CtClass ctClass = ClassPool.getDefault().makeClass(new ByteArrayInputStream(classData), false);

      CtConstructor initializer = ctClass.getClassInitializer();
      if(initializer == null) {
        initializer = ctClass.makeClassInitializer();
      }

      initializer.insertAfter(
          "net.labyfy.component.transform.launchplugin.NotifyJumpad.notifyService(" +
              ctClass.getName() + ".class" +
          ");"
      );

      return ctClass.toBytecode();
    } catch (IOException e) {
      throw new RuntimeException("Failed to modify class due to IOException", e);
    } catch (CannotCompileException e) {
      logger.warn("Failed to modify class due to compilation error", e);
      return null;
    }
  }

  public void registerTransformer(LateInjectedTransformer transformer) {
    injectedTransformers.add(transformer);
  }
}
