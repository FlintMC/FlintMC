package net.labyfy.internal.component.config.generator.service;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.generator.ConfigGenerator;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.processing.autoload.identifier.Identifier;
import net.labyfy.component.stereotype.service.CtResolver;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;

import java.io.IOException;

@Singleton
@Service(value = Config.class, priority = 1)
public class ConfigGenerationService implements ServiceHandler<Config> {

  private final ConfigGenerator generator;

  @Inject
  public ConfigGenerationService(ConfigGenerator generator) {
    this.generator = generator;
  }

  @Override
  public void discover(AnnotationMeta<Config> meta) throws ServiceNotFoundException {
    Identifier<CtClass> identifier = meta.getIdentifier();
    CtClass location = identifier.getLocation();

    try {
      Object implementation = this.generator.generateConfigImplementation(location);

      if (implementation == null) {
        return;
      }

      Class<?> base = CtResolver.get(location);

      InjectionHolder.getInstance().addModules(new AbstractModule() {
        @Override
        protected void configure() {
          super.bind((Class) base).toInstance(implementation);
        }
      });

    } catch (NotFoundException | CannotCompileException | IOException | ReflectiveOperationException e) {
      throw new ServiceNotFoundException("Cannot generate config for " + location.getName(), e);
    }
  }
}
