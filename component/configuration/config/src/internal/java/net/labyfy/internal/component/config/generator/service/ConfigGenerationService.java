package net.labyfy.internal.component.config.generator.service;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.generator.ConfigGenerator;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.identifier.LocatedIdentifiedAnnotation;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;

import java.io.IOException;

@Singleton
@Service(Config.class)
public class ConfigGenerationService implements ServiceHandler {

  private final ConfigGenerator generator;

  @Inject
  public ConfigGenerationService(ConfigGenerator generator) {
    this.generator = generator;
  }

  @Override
  public void discover(Identifier.Base property) throws ServiceNotFoundException {

    LocatedIdentifiedAnnotation annotation = property.getProperty().getLocatedIdentifiedAnnotation();
    Class<?> locationClass = annotation.getLocation();
    String name = locationClass.getName();

    try {
      CtClass type = ClassPool.getDefault().get(name);

      Class<?> base = super.getClass().getClassLoader().loadClass(type.getName());
      Object implementation = this.generator.generateConfigImplementation(type);

      if (implementation == null) {
        return;
      }

      InjectionHolder.getInstance().addModules(new AbstractModule() {
        @Override
        protected void configure() {
          super.bind((Class) base).toInstance(implementation);
        }
      });

    } catch (NotFoundException | CannotCompileException | IOException | ReflectiveOperationException e) {
      throw new ServiceNotFoundException("Cannot generate config for " + name, e);
    }

  }

}
