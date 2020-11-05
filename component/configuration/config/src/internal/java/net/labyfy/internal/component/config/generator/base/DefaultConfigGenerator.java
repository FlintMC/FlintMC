package net.labyfy.internal.component.config.generator.base;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.annotation.PostMinecraftRead;
import net.labyfy.component.config.event.ConfigDiscoveredEvent;
import net.labyfy.component.config.generator.ConfigGenerator;
import net.labyfy.component.config.generator.ConfigImplementer;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.config.storage.ConfigStorageProvider;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.implement.Implement;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Implement(ConfigGenerator.class)
public class DefaultConfigGenerator implements ConfigGenerator {

  private final ConfigStorageProvider storageProvider;
  private final ConfigObjectReference.Parser objectReferenceParser;
  private final GeneratingConfig.Factory configFactory;
  private final ConfigImplementer configImplementer;
  private final EventBus eventBus;
  private final ConfigDiscoveredEvent.Factory eventFactory;

  private final ImplementationGenerator implementationGenerator;

  private final Map<String, ParsedConfig> discoveredConfigs;

  @Inject
  public DefaultConfigGenerator(ConfigStorageProvider storageProvider, ConfigObjectReference.Parser objectReferenceParser,
                                GeneratingConfig.Factory configFactory, ConfigImplementer configImplementer,
                                EventBus eventBus, ConfigDiscoveredEvent.Factory eventFactory,
                                ImplementationGenerator implementationGenerator) {
    this.storageProvider = storageProvider;
    this.objectReferenceParser = objectReferenceParser;
    this.configFactory = configFactory;
    this.configImplementer = configImplementer;
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
    this.implementationGenerator = implementationGenerator;

    this.discoveredConfigs = new HashMap<>();
  }

  @Override
  public ParsedConfig generateConfigImplementation(CtClass configInterface) throws NotFoundException, CannotCompileException, IOException, ReflectiveOperationException {
    if (!configInterface.hasAnnotation(Config.class)) {
      throw new IllegalArgumentException("Cannot generate config from " + configInterface.getName()
          + " without it having the @Config annotation");
    }
    if (this.discoveredConfigs.containsKey(configInterface.getName())) {
      return this.discoveredConfigs.get(configInterface.getName());
    }

    ConfigClassLoader classLoader = this.implementationGenerator.getClassLoader();
    GeneratingConfig config = this.configFactory.create(configInterface);

    CtClass implementation = this.implementationGenerator.implementConfig(configInterface, config);
    if (implementation == null) {
      return null;
    }

    this.configImplementer.implementParsedConfig(implementation, config.getName());

    for (CtClass generated : config.getGeneratedImplementations()) {
      classLoader.defineClass(generated.getName(), generated.toBytecode());
    }

    ParsedConfig result = (ParsedConfig) classLoader.loadClass(implementation.getName()).getDeclaredConstructor().newInstance();

    this.bindConfig(config, result);

    return result;
  }

  @Override
  public Collection<ParsedConfig> getDiscoveredConfigs() {
    return Collections.unmodifiableCollection(this.discoveredConfigs.values());
  }

  @Override
  public void bindConfig(GeneratingConfig generatingConfig, ParsedConfig config) throws IllegalStateException {
    if (this.discoveredConfigs.containsKey(config.getConfigName())) {
      throw new IllegalStateException("Config with the name " + config.getConfigName() + " is already registered");
    }

    Collection<ConfigObjectReference> references = this.objectReferenceParser.parseAll(generatingConfig);
    config.getConfigReferences().addAll(references);

    if (!config.getClass().isAnnotationPresent(PostMinecraftRead.class)) {
      this.storageProvider.read(config);
    }

    this.discoveredConfigs.put(config.getConfigName(), config);
    this.eventBus.fireEvent(this.eventFactory.create(config), Subscribe.Phase.POST);
  }

}
