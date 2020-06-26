package net.labyfy.internal.component.inject;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.inject.primitive.InjectionHolder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Utility base class for common injection service operations.
 */
public class InjectionServiceShare {
  private static final Collection<Class> implementationsFlushed = new HashSet<>();
  private static final Collection<Class> assistedFlushed = new HashSet<>();
  private static final Collection<Class> ignoreFlushed = new HashSet<>();

  protected static final Map<Class, Class> implementations = Maps.newHashMap();
  protected static final Map<Class, AssistedFactory> assisted = new HashMap<>();
  protected static final Collection<Class> ignore = new HashSet<>();

  /**
   * Retrieves all ignored classes.
   *
   * @return All ignored classes
   */
  public static Collection<Class> getIgnore() {
    return ignore;
  }

  /**
   * Retrieves the map describing the associations between classes and their assisted factories.
   *
   * @return Mapping of classes and their assisted factories
   */
  public static Map<Class, AssistedFactory> getAssisted() {
    return assisted;
  }


  /**
   * Retrieves the map describing the associations between classes and their implementations.
   *
   * @return Mapping of classes and their implementations
   */
  public static Map<Class, Class> getImplementations() {
    return implementations;
  }

  /**
   * Flushes all cached changes to Guice
   */
  public static void flush() {
    InjectionHolder.getInstance()
        .addModules(
            new AbstractModule() {
              protected void configure() {
                // Flush all implementations
                implementations.forEach(
                    (superClass, implementation) -> {
                      if (!ignore.contains(superClass) && !ignore.contains(implementation) && !implementationsFlushed.contains(implementation)) {
                        this.bind(superClass).to(implementation);
                        implementationsFlushed.add(implementation);
                      }
                    });

                // Flush all factories
                assisted.forEach(
                    (clazz, factory) -> {
                      if (!assistedFlushed.contains(clazz)) {
                        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();
                        implementations.forEach(factoryModuleBuilder::implement);
                        install(factoryModuleBuilder.build(clazz));
                        assistedFlushed.add(clazz);
                      }
                    });
              }
            });
  }
}
