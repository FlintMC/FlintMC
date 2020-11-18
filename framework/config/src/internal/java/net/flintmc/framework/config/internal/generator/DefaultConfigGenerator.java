package net.flintmc.framework.config.internal.generator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.annotation.PostMinecraftRead;
import net.flintmc.framework.config.event.ConfigDiscoveredEvent;
import net.flintmc.framework.config.generator.ConfigGenerator;
import net.flintmc.framework.config.generator.ConfigImplementer;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.internal.generator.base.ConfigClassLoader;
import net.flintmc.framework.config.internal.generator.base.ImplementationGenerator;
import net.flintmc.framework.config.storage.ConfigStorageProvider;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;

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
  public DefaultConfigGenerator(
      ConfigStorageProvider storageProvider,
      ConfigObjectReference.Parser objectReferenceParser,
      GeneratingConfig.Factory configFactory,
      ConfigImplementer configImplementer,
      EventBus eventBus,
      ConfigDiscoveredEvent.Factory eventFactory,
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
  public ParsedConfig generateConfigImplementation(CtClass configInterface)
      throws NotFoundException, CannotCompileException, IOException, ReflectiveOperationException {
    if (!configInterface.hasAnnotation(Config.class)) {
      throw new IllegalArgumentException(
          "Cannot generate config from "
              + configInterface.getName()
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

    // add the ParsedConfig interface to the base implementation (annotated with @Config)
    this.configImplementer.implementParsedConfig(implementation, config.getName());

    // load the interfaces so that the transformers will be applied
    for (CtClass implementedInterface : config.getImplementedInterfaces()) {
      this.loadAllClasses(classLoader, implementedInterface);
    }

    // define the newly generated classes
    for (CtClass generated : config.getGeneratedImplementations()) {
      classLoader.defineClass(generated.getName(), generated.toBytecode());
    }

    ParsedConfig result =
        (ParsedConfig)
            classLoader.loadClass(implementation.getName()).getDeclaredConstructor().newInstance();

    this.bindConfig(config, result);

    return result;
  }

  private void loadAllClasses(ClassLoader classLoader, CtClass implemented)
      throws ClassNotFoundException, NotFoundException {
    // load all inner classes first so that they don't get frozen when loading the outer classes
    for (CtClass declared : implemented.getDeclaredClasses()) {
      this.loadAllClasses(classLoader, declared);
    }

    classLoader.loadClass(implemented.getName());
  }

  @Override
  public Collection<ParsedConfig> getDiscoveredConfigs() {
    return Collections.unmodifiableCollection(this.discoveredConfigs.values());
  }

  @Override
  public void bindConfig(GeneratingConfig generatingConfig, ParsedConfig config)
      throws IllegalStateException {
    if (this.discoveredConfigs.containsKey(config.getConfigName())) {
      throw new IllegalStateException(
          "Config with the name " + config.getConfigName() + " is already registered");
    }

    Collection<ConfigObjectReference> references =
        this.objectReferenceParser.parseAll(generatingConfig, config);
    config.getConfigReferences().addAll(references);

    if (!config.getClass().isAnnotationPresent(PostMinecraftRead.class)) {
      this.initConfig(config);
    }

    this.discoveredConfigs.put(config.getConfigName(), config);
    this.eventBus.fireEvent(this.eventFactory.create(config), Subscribe.Phase.POST);
  }

  @Override
  public void initConfig(ParsedConfig config) {
    for (ConfigObjectReference reference : config.getConfigReferences()) {
      Object defaultValue = reference.getDefaultValue();
      if (defaultValue != null) {
        reference.setValue(defaultValue);
      }
    }

    this.storageProvider.read(config);
  }
}
