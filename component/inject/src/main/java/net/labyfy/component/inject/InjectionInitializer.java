package net.labyfy.component.inject;

import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import net.labyfy.base.structure.Initialize;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.identifier.IdentifierParser;
import net.labyfy.base.structure.property.Property;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.inject.implement.Implement;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class InjectionInitializer {

  @Initialize(priority = -1000)
  public static void init() throws IOException, ClassNotFoundException {
    Collection<Class<?>> classes = new HashSet<>();

    for (ClassPath.ClassInfo clazz :
        ClassPath.from(InjectionInitializer.class.getClassLoader()).getAllClasses()) {

      if (clazz.getName().startsWith("net.labyfy")) {
        classes.add(clazz.load());
      }
    }

    Map<Class, Class> implementations = Maps.newHashMap();

    for (Class<?> clazz : classes) {
      if (!clazz.isAnnotationPresent(Implement.class)) continue;

      Implement declaredAnnotation = clazz.getDeclaredAnnotation(Implement.class);

      if (!(declaredAnnotation.version().isEmpty()
          || InjectionHolder.getInstance()
              .getInjector()
              .getInstance(Key.get(Map.class, Names.named("launchArguments")))
              .get("--version")
              .equals(declaredAnnotation.version()))) continue;

      implementations.put(declaredAnnotation.value(), clazz);
    }

    InjectionHolder.getInstance()
        .addModules(
            new AbstractModule() {
              protected void configure() {
                implementations.forEach(
                    (superClass, implementation) -> this.bind(superClass).to(implementation));
              }
            });

    for (Class<?> clazz : classes) {
      if (clazz.getName().startsWith("net.labyfy")
          && clazz.isAnnotationPresent(AssistedFactory.class)) {
        Class.forName(clazz.getName(), true, InjectionHolder.class.getClassLoader());

        Collection<Identifier.Base> parse =
            InjectionHolder.getInstance()
                .getInjector()
                .getInstance(IdentifierParser.class)
                .parse(clazz);

        for (Identifier.Base base : parse) {
          Annotation annotation =
              base.getProperty().getLocatedIdentifiedAnnotation().getAnnotation();
          if (annotation instanceof AssistedFactory) {
            Property.Base property = base.getProperty();
            FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();
            implementations.forEach(factoryModuleBuilder::implement);
            InjectionHolder.getInstance()
                .addModules(
                    factoryModuleBuilder.build(
                        property.getLocatedIdentifiedAnnotation().<Class<?>>getLocation()));
          }
        }
      }
    }
  }
}
