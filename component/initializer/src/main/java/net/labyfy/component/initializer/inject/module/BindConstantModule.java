package net.labyfy.component.initializer.inject.module;

import com.google.common.eventbus.EventBus;
import com.google.common.reflect.ClassPath;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
public class BindConstantModule extends AbstractModule {

  protected void configure() {
    this.bindNamedFilePath("minotarUrl", "https://minotar.net/helm/%s/16.png");
    this.bindNamedFilePath("labyModVersion", "4.0.0");

    this.bindNamedFilePath("minecraftRoot", "./");

    this.bindNamedFilePath("labyModRoot", "./LabyMod4");

    this.bindNamedFilePath("labyModAddons", "./LabyMod4/addons");


    this.bind(EventBus.class).toInstance(new EventBus("labymod"));
    this.bind(ExecutorService.class).to(ScheduledExecutorService.class);
    this.bind(ScheduledExecutorService.class).toInstance(Executors.newScheduledThreadPool(0));
  }

  private void bindNamedFilePath(String name, String path) {
    this.bindNamed(name, path);
    this.bindNamed(name, new File(path));
  }

  private void bindNamed(String name, Object object) {
    this.bind(Key.get(((Class) object.getClass()), Names.named(name))).toInstance(object);
  }
}
