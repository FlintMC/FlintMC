package net.labyfy.component.initializer;

import com.google.inject.*;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import javafx.application.Platform;
import net.labyfy.base.structure.service.ServiceRepository;
import net.labyfy.component.initializer.inject.InitializationModule;
import net.labyfy.component.initializer.inject.LabyInjectionInitializer;

public class EntryPoint {

  private static final ReentrantLock lock = new ReentrantLock();
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
                  while (!initialize.isEmpty()) {
                    Class poll = initialize.poll();
                    injector.getInstance(ServiceRepository.class).notifyClassLoaded(poll);
                  }
                  synchronized (EntryPoint.class) {
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
    initialize.add(clazz);
    if (!(lock.isLocked() && !lock.isHeldByCurrentThread())) {
      synchronized (EntryPoint.class) {
        if (updateThread != null) {
          EntryPoint.class.notify();
        }
      }
    }
  }
}
