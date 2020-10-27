package net.labyfy.component.transform.launchplugin.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.name.Names;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.inject.util.ContextAwareProvisionListener;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.launcher.classloading.RootClassLoader;
import net.labyfy.component.transform.launchplugin.inject.logging.AnnotatedLoggerTypeListener;
import net.labyfy.component.transform.launchplugin.inject.logging.LoggerTypeListener;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * This class binds constants, so that they can be used with @named
 */
public class BindConstantModule extends AbstractModule {

  private static final int SCHEDULED_POOL_SIZE = 2;

  private final Map<String, String> launchArguments;

  public BindConstantModule(Map<String, String> launchArguments) {
    this.launchArguments = launchArguments;
  }

  protected void configure() {
    this.bindNamedFilePath("labyfyPackageFolder", "./Labyfy/packages");
    this.bindNamedFilePath("labyfyRoot", "./Labyfy");
    this.bindNamedFilePath("labyfyThemesRoot", "./Labyfy/themes");
    this.bindNamed("delegationClassLoader", LaunchController.getInstance().getRootLoader());
    this.bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());
    this.bind(ScheduledExecutorService.class).toInstance(Executors.newScheduledThreadPool(SCHEDULED_POOL_SIZE));
    boolean obfuscated = ((RootClassLoader) getClass().getClassLoader()).findResource("net/minecraft/client/Minecraft.class") == null;

    this.bindNamed("obfuscated", obfuscated);
    this.bind(Key.get(Map.class, Names.named("launchArguments"))).toInstance(this.launchArguments);
    ContextAwareProvisionListener.bindContextAwareProvider(this.binder(), Key.get(Logger.class, InjectLogger.class), new AnnotatedLoggerTypeListener());
    ContextAwareProvisionListener.bindContextAwareProvider(this.binder(), Logger.class, new LoggerTypeListener());

  }

  private void bindNamedFilePath(String name, String path) {
    this.bindNamed(name, path);
    this.bindNamed(name, new File(path));
  }

  private void bindNamed(String name, Object object) {
    this.bind(Key.get(((Class) object.getClass()), Names.named(name))).toInstance(object);
  }
}
