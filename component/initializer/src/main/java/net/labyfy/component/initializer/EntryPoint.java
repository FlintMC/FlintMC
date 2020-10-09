package net.labyfy.component.initializer;

import javassist.CtClass;
import net.labyfy.component.initializer.inject.LabyInjectionInitializer;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.component.stereotype.service.ServiceRepository;

import java.util.Map;

public class EntryPoint {

  private static boolean initialized;

  public EntryPoint(Map<String, String> launchArguments) {

    InjectionHolder.getInjectedInstance(LabyInjectionInitializer.class);
    InjectionHolder.getInjectedInstance((ServiceRepository.class));
    initialized = true;
  }

  /**
   * Notifis all services that a new class is loaded.
   *
   * @param clazz The loaded class to notify
   * @throws ServiceNotFoundException If the service could not be discovered.
   */
  public static void notifyService(CtClass ctClass) throws ServiceNotFoundException {
    if (initialized)
      InjectionHolder.getInjectedInstance(ServiceRepository.class).notifyClassLoaded(ctClass);
  }
}
