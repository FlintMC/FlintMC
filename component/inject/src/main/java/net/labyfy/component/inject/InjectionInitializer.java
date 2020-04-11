package net.labyfy.component.inject;

import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import groovy.lang.Script;
import net.labyfy.base.structure.Initialize;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.identifier.IdentifierParser;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.base.structure.property.Property;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.inject.implement.Implement;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class InjectionInitializer {

  @Initialize(priority = -1000)
  public static void init() throws IOException, ClassNotFoundException {
    Collection<Class<?>> classes = new HashSet<>();

    for (ClassPath.ClassInfo clazz :
        ClassPath.from(InjectionInitializer.class.getClassLoader()).getAllClasses()) {

      if (clazz.getName().startsWith("net.labyfy")) {
        Class<?> loadedClass = clazz.load();
        if ((loadedClass.getSuperclass() != null
                && loadedClass.getSuperclass().getName().contains("groovy"))
            || loadedClass.getName().contains("groovy")) continue;
        if (loadedClass.isAnnotationPresent(IgnoreInitialization.class)) continue;
        classes.add(clazz.load());
      }
    }

    Map<Class, Class> implementations = Maps.newHashMap();
    Collection<Class> ignore = new HashSet<>();
    Map<Class, AssistedFactory> assisted = new HashMap<>();

    for (Class<?> clazz : classes) {
      if (!clazz.isAnnotationPresent(Implement.class)) continue;

      Implement declaredAnnotation = clazz.getDeclaredAnnotation(Implement.class);

      if (!(declaredAnnotation.version().isEmpty()
          || InjectionHolder.getInjectedInstance(Key.get(Map.class, Names.named("launchArguments")))
              .get("--version")
              .equals(declaredAnnotation.version()))) continue;

      implementations.put(declaredAnnotation.value(), clazz);
    }

    Injector injector = Guice.createInjector();
    IdentifierParser instance = injector.getInstance(IdentifierParser.class);

    for (Class<?> clazz : classes) {
      Collection<Identifier.Base> parse = instance.parse(clazz);
      for (Identifier.Base base : parse) {
        Annotation annotation = base.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
        if (annotation instanceof AssistedFactory) {
          assisted.put(clazz, (AssistedFactory) annotation);
          ignore.add(((AssistedFactory) annotation).value());
        }
      }
    }

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
