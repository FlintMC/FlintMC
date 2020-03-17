package net.labyfy.component.inject;

import com.google.common.reflect.ClassPath;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import net.labyfy.base.structure.Initialize;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.identifier.IdentifierParser;
import net.labyfy.base.structure.property.Property;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.io.IOException;
import java.util.Collection;

public class InjectionInitializer {

  @Initialize(priority = -1000)
  public static void init() throws IOException, ClassNotFoundException {
    for (ClassPath.ClassInfo clazz :
        ClassPath.from(InjectionInitializer.class.getClassLoader()).getAllClasses()) {
      if (clazz.getName().startsWith("net.labyfy")
          && clazz.load().isAnnotationPresent(AssistedFactory.class)) {
        Class.forName(clazz.getName(), true, InjectionHolder.class.getClassLoader());

        Class<?> load = clazz.load();
        Collection<Identifier.Base> parse =
            InjectionHolder.getInstance()
                .getInjector()
                .getInstance(IdentifierParser.class)
                .parse(load);

        for (Identifier.Base base : parse) {
          if (base.getProperty().getLocatedIdentifiedAnnotation().getAnnotation()
              instanceof AssistedFactory) {
            Property.Base property = base.getProperty();
            InjectionHolder.getInstance()
                .addModules(
                    new FactoryModuleBuilder()
                        .build(property.getLocatedIdentifiedAnnotation().<Class<?>>getLocation()));
          }
        }
      }
    }
  }
}
