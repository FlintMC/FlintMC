package net.labyfy.internal.component.config.transform;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import net.labyfy.component.config.annotation.implemented.ConfigImplementation;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;

import java.util.Map;

@Service(value = ConfigImplementation.class, priority = 3 /* needs to be called after the ConfigTransformer */)
public class ConfigPostTransformer implements ServiceHandler<ConfigImplementation> {

  private final ConfigTransformer transformer;

  @Inject
  public ConfigPostTransformer(ConfigTransformer transformer) {
    this.transformer = transformer;
  }

  @Override
  public void discover(AnnotationMeta<ConfigImplementation> meta) {
    // flush the implemented configs from the ConfigTransformer
    // into the injector

    Map<Class<?>, Class<?>> mappings = this.transformer.getMappings();
    if (mappings.isEmpty()) {
      return;
    }

    InjectionHolder.getInstance().addModules(new AbstractModule() {
      @Override
      protected void configure() {
        mappings.forEach((superClass, implementation) -> super.bind((Class) superClass).to(implementation));
        mappings.clear();
      }
    });
  }
}
