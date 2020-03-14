package net.labyfy.component.initializer;

import com.google.inject.*;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Platform;
import net.labyfy.base.structure.service.ServiceRepository;
import net.labyfy.component.initializer.inject.InitializationModule;
import net.labyfy.component.initializer.inject.LabyInjectionInitializer;

public class EntryPoint {

  private static Injector injector;
  private static Queue<Class> initialize = new LinkedBlockingQueue<>();
  private static Thread updateThread;

  public EntryPoint(Map<String, String> launchArguments) {
    injector =
        Guice.createInjector(
            InitializationModule.create(
                Collections.emptyList(), new AtomicReference<>(), launchArguments));
    injector.getInstance(LabyInjectionInitializer.class);
    injector.getInstance(ServiceRepository.class);
    updateThread =
        new Thread(
            () -> {
              while (true) {
                try {
                  synchronized (EntryPoint.class) {
                    while (!initialize.isEmpty()) {
                      Class poll = initialize.poll();
                      injector.getInstance(ServiceRepository.class).notifyClassLoaded(poll);
                    }
                    EntryPoint.class.wait();
                  }
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            });

    updateThread.start();
  }

  public static void notifyService(Class clazz) {
    if (updateThread == null) return;
    synchronized (EntryPoint.class) {
      initialize.add(clazz);
      EntryPoint.class.notify();
    }
  }
}
