package net.labyfy.internal.component.config.transform;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import net.labyfy.component.config.annotation.implemented.ConfigImplementation;
import net.labyfy.component.config.generator.ConfigGenerator;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;

import java.util.ArrayList;
import java.util.Collection;

@Service(value = ConfigImplementation.class, priority = 3 /* needs to be called after the ConfigTransformer */)
public class ConfigPostTransformer implements ServiceHandler<ConfigImplementation> {

  private final ConfigGenerator configGenerator;
  private final ConfigTransformer transformer;

  @Inject
  public ConfigPostTransformer(ConfigGenerator configGenerator, ConfigTransformer transformer) {
    this.configGenerator = configGenerator;
    this.transformer = transformer;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void discover(AnnotationMeta<ConfigImplementation> annotationMeta) {
    // flush the implemented configs from the ConfigTransformer
    // into the injector

    Collection<TransformedConfigMeta> mappings = this.transformer.getMappings();
    if (mappings.isEmpty()) {
      return;
    }

    Collection<TransformedConfigMeta> pendingConfigs = new ArrayList<>();

    InjectionHolder.getInstance().addModules(new AbstractModule() {
      @Override
      protected void configure() {
        for (TransformedConfigMeta meta : mappings) {
          Class<?> superClass = meta.getSuperClass();
          Class<?> implementation = meta.getImplementationClass();

          super.bind((Class) superClass).to(implementation);

          if (ParsedConfig.class.isAssignableFrom(implementation)) {
            pendingConfigs.add(meta);
          }
        }

        mappings.clear();
      }
    });

    // load the module so that the pendingConfigs will be filled
    InjectionHolder.getInstance().getInjector();

    // register the configs in the ConfigGenerator after the module has been configured
    for (TransformedConfigMeta meta : pendingConfigs) {
      this.configGenerator.bindConfig(meta.getConfig(), (ParsedConfig) InjectionHolder.getInjectedInstance(meta.getSuperClass()));
    }
  }
}
