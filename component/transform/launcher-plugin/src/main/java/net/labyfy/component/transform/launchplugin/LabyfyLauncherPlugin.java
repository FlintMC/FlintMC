package net.labyfy.component.transform.launchplugin;

import javassist.*;
import net.labyfy.component.launcher.classloading.RootClassloader;
import net.labyfy.component.launcher.service.LabyLauncherPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LabyfyLauncherPlugin implements LabyLauncherPlugin {
  private final Logger logger;

  public LabyfyLauncherPlugin() {
    this.logger = LogManager.getLogger(LabyfyLauncherPlugin.class);
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
  public void preLaunch(ClassLoader launchClassloader) {
    try {
      launchClassloader
          .loadClass("net.labyfy.component.initializer.EntryPoint")
          .getDeclaredConstructors()[0]
          .newInstance();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Failed to instantiate initializer", e);
    }

    try {
      Class<?> initializerClass = launchClassloader.loadClass("net.labyfy.base.structure.Initializer");
      Method boot = initializerClass.getDeclaredMethod("boot");
      boot.invoke(null);
    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Failed to invoke initializer", e);
    }

    try {
      Class<?> injectionHolderClass = launchClassloader.loadClass("net.labyfy.component.inject.InjectionHolder");
      Method enableIngameState = injectionHolderClass.getDeclaredMethod("enableIngameState");
      enableIngameState.invoke(null);
    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Failed to enable ingame state", e);
    }
  }

  @Override
  public byte[] modifyClass(byte[] classData) {
    try {
      CtClass ctClass = ClassPool.getDefault().makeClass(new ByteArrayInputStream(classData));

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
}
