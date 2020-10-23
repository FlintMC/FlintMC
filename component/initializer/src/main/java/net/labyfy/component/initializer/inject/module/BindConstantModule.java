package net.labyfy.component.initializer.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.launcher.classloading.RootClassLoader;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * This class binds constants, so the can be used with @named
 */
@Singleton
public class BindConstantModule extends AbstractModule {

  private static final int SCHEDULED_POOL_SIZE = 2;

  protected void configure() {
    this.bindNamedFilePath("labyfyPackageFolder", "./Labyfy/packages");
    this.bindNamedFilePath("labyfyRoot", "./Labyfy");
    this.bindNamedFilePath("labyfyThemesRoot", "./Labyfy/themes");
    this.bindNamed("delegationClassLoader", LaunchController.getInstance().getRootLoader());
    this.bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());
    this.bind(ScheduledExecutorService.class).toInstance(Executors.newScheduledThreadPool(SCHEDULED_POOL_SIZE));
    boolean obfuscated = ((RootClassLoader) getClass().getClassLoader()).findResource("net/minecraft/client/Minecraft.class") == null;

    this.bindNamed("obfuscated", obfuscated);
  }

  private void bindNamedFilePath(String name, String path) {
    this.bindNamed(name, path);
    this.bindNamed(name, new File(path));
  }

  private void bindNamed(String name, Object object) {
    this.bind(Key.get(((Class) object.getClass()), Names.named(name))).toInstance(object);
  }
}
