package net.labyfy.component.initializer;

import com.google.inject.*;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import net.labyfy.base.structure.service.ServiceRepository;
import net.labyfy.component.initializer.inject.InitializationModule;
import net.labyfy.component.initializer.inject.LabyInjectionInitializer;
import net.labyfy.component.inject.InjectionHolder;

public class EntryPoint {

  private static boolean initialized;

  public EntryPoint(Map<String, String> launchArguments) {
    InjectionHolder.getInstance()
        .addModules(InitializationModule.create(new AtomicReference<>(), launchArguments));

    Injector injector = InjectionHolder.getInstance().getInjector();

    injector.getInstance(LabyInjectionInitializer.class);
    injector.getInstance(ServiceRepository.class);
    initialized = true;
  }

  public static void notifyService(Class clazz) {
    if (initialized)
      InjectionHolder.getInstance()
          .getInjector()
          .getInstance(ServiceRepository.class)
          .notifyClassLoaded(clazz);
  }
}
