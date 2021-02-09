/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.framework.config.internal.generator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.config.EventConfigInitializer;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.annotation.ConfigInit;
import net.flintmc.framework.config.event.ConfigDiscoveredEvent;
import net.flintmc.framework.config.generator.ConfigAnnotationCollector;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

  private final ConfigAnnotationCollector annotationCollector;

  private final ImplementationGenerator implementationGenerator;

  private final Map<String, ParsedConfig> discoveredConfigs;

  private final EventConfigInitializer eventConfigInitializer;

  @Inject
  private DefaultConfigGenerator(
      ConfigStorageProvider storageProvider,
      ConfigObjectReference.Parser objectReferenceParser,
      GeneratingConfig.Factory configFactory,
      ConfigImplementer configImplementer,
      EventBus eventBus,
      ConfigDiscoveredEvent.Factory eventFactory,
      ConfigAnnotationCollector annotationCollector,
      ImplementationGenerator implementationGenerator,
      EventConfigInitializer eventConfigInitializer) {
    this.storageProvider = storageProvider;
    this.objectReferenceParser = objectReferenceParser;
    this.configFactory = configFactory;
    this.configImplementer = configImplementer;
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
    this.annotationCollector = annotationCollector;
    this.implementationGenerator = implementationGenerator;
    this.eventConfigInitializer = eventConfigInitializer;

    this.discoveredConfigs = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<ParsedConfig> getDiscoveredConfigs() {
    return Collections.unmodifiableCollection(this.discoveredConfigs.values());
  }

  /**
   * {@inheritDoc}
   */
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

    List<ConfigInit> configInits =
        new ArrayList<>(
            this.annotationCollector.getAllAnnotations(config.getClass(), ConfigInit.class));
    if (configInits.isEmpty()) {
      // no initialization conditions are given, we can initialize now
      this.initConfig(config);
    } else {
      // registering the config for later initialization after the configured event is fired
      this.eventConfigInitializer.registerPendingInitialization(config, configInits.get(0));
    }

    this.discoveredConfigs.put(config.getConfigName(), config);
    this.eventBus.fireEvent(this.eventFactory.create(config), Subscribe.Phase.POST);
  }

  /**
   * {@inheritDoc}
   */
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
