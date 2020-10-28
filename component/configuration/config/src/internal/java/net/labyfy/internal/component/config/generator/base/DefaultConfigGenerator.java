package net.labyfy.internal.component.config.generator.base;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.event.ConfigDiscoveredEvent;
import net.labyfy.component.config.generator.ConfigGenerator;
import net.labyfy.component.config.generator.ConfigImplementer;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.config.storage.ConfigStorageProvider;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.internal.component.config.transform.ConfigTransformer;

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
  private final ConfigTransformer transformer;

  private final Map<String, ParsedConfig> discoveredConfigs;

  @Inject
  public DefaultConfigGenerator(ConfigStorageProvider storageProvider, ConfigObjectReference.Parser objectReferenceParser,
                                GeneratingConfig.Factory configFactory, ConfigImplementer configImplementer,
                                EventBus eventBus, ConfigDiscoveredEvent.Factory eventFactory,
                                ImplementationGenerator implementationGenerator, ConfigTransformer transformer) {
    this.storageProvider = storageProvider;
    this.objectReferenceParser = objectReferenceParser;
    this.configFactory = configFactory;
    this.configImplementer = configImplementer;
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
    this.implementationGenerator = implementationGenerator;
    this.transformer = transformer;

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

    CtClass implementation = this.generateImplementation(classLoader, configInterface, config);
    if (implementation == null) {
      return null;
    }

    this.configImplementer.implementParsedConfig(implementation, config.getName());

    Collection<ConfigObjectReference> references = this.objectReferenceParser.parseAll(config);

    ParsedConfig result = (ParsedConfig) classLoader.loadClass(implementation.getName()).getDeclaredConstructor().newInstance();
    result.getConfigReferences().addAll(references);
    this.storageProvider.read(result);

    this.discoveredConfigs.put(configInterface.getName(), result);
    this.eventBus.fireEvent(this.eventFactory.create(result), Subscribe.Phase.POST);

    return result;
  }

  private CtClass generateImplementation(ConfigClassLoader classLoader, CtClass configInterface, GeneratingConfig config)
      throws NotFoundException, CannotCompileException, IOException {
    CtClass implementation = this.implementationGenerator.implementConfig(configInterface, config);
    if (implementation == null) {
      for (ConfigMethod method : config.getAllMethods()) {
        this.transformer.addPendingTransform(method);
      }
      return null;
    }

    for (CtClass generated : config.getGeneratedImplementations()) {
      classLoader.defineClass(generated.getName(), generated.toBytecode());
    }

    return implementation;
  }

  @Override
  public Collection<ParsedConfig> getDiscoveredConfigs() {
    return Collections.unmodifiableCollection(this.discoveredConfigs.values());
  }

}
