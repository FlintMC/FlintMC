package net.labyfy.component.initializer;

import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.inject.ServiceRepository;
import net.labyfy.component.initializer.inject.InitializationModule;
import net.labyfy.component.initializer.inject.LabyInjectionInitializer;
import net.labyfy.component.inject.InjectionHolder;

import java.util.Map;

public class EntryPoint {

  private static boolean initialized;

  public EntryPoint(Map<String, String> launchArguments) {
    InjectionHolder.getInstance()
        .addModules(
            InitializationModule.create(
                InjectionHolder.getInstance().getInjectorReference(), launchArguments));

    InjectionHolder.getInjectedInstance(LabyInjectionInitializer.class);
    InjectionHolder.getInjectedInstance((ServiceRepository.class));
    initialized = true;
  }

  public static void notifyService(Class clazz) {
    if ((clazz.getSuperclass() != null && clazz.getSuperclass().getName().contains("groovy"))
        || clazz.getName().contains("groovy")) return;
    if (clazz.isAnnotationPresent(IgnoreInitialization.class)) return;

    if (initialized)
      InjectionHolder.getInjectedInstance(ServiceRepository.class).notifyClassLoaded(clazz);
  }
}
