package net.labyfy.component.inject;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class InjectionServiceShare {

  protected static final Map<Class, Class> implementations = Maps.newHashMap();
  protected static final Map<Class, AssistedFactory> assisted = new HashMap<>();
  protected static final Collection<Class> ignore = new HashSet<>();
  private static boolean flushed;

  public static Collection<Class> getIgnore() {
    return ignore;
  }

  public static Map<Class, AssistedFactory> getAssisted() {
    return assisted;
  }

  public static Map<Class, Class> getImplementations() {
    return implementations;
  }

  public static void flush() {
    if (flushed) return;
    flushed = true;

    InjectionHolder.getInstance()
        .addModules(
            new AbstractModule() {
              protected void configure() {
                implementations.forEach(
                    (superClass, implementation) -> {
                      if (!ignore.contains(superClass) && !ignore.contains(implementation)) {
                        this.bind(superClass).to(implementation);
                      }
                    });

                assisted.forEach(
                    (clazz, factory) -> {
                      FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();
                      implementations.forEach(factoryModuleBuilder::implement);
                      install(factoryModuleBuilder.build(clazz));
                    });
              }
            });

  }
}
