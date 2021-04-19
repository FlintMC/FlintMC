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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.Modifier;
import javassist.NotFoundException;
import net.flintmc.framework.config.EventConfigInitializer;
import net.flintmc.framework.config.annotation.ConfigInit;
import net.flintmc.framework.config.event.ConfigDiscoveredEvent;
import net.flintmc.framework.config.generator.ConfigAnnotationCollector;
import net.flintmc.framework.config.generator.ConfigGenerator;
import net.flintmc.framework.config.generator.ConfigImplementer;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.GeneratingConfig.Factory;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.generator.method.ConfigObjectReference.Parser;
import net.flintmc.framework.config.internal.generator.base.ImplementationGenerator;
import net.flintmc.framework.config.internal.generator.instance.ConfigInstanceCreator;
import net.flintmc.framework.config.internal.generator.instance.ConfigInstanceCreatorFactory;
import net.flintmc.framework.config.storage.ConfigStorageProvider;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.launcher.LaunchController;

@Singleton
@Implement(ConfigGenerator.class)
@SuppressWarnings("unchecked")
public class DefaultConfigGenerator implements ConfigGenerator {

  private final ConfigStorageProvider storageProvider;
  private final ConfigObjectReference.Parser objectReferenceParser;
  private final GeneratingConfig.Factory configFactory;
  private final ConfigImplementer configImplementer;
  private final EventBus eventBus;
  private final ConfigDiscoveredEvent.Factory eventFactory;

  private final ConfigAnnotationCollector annotationCollector;

  private final ImplementationGenerator implementationGenerator;

  private final ConfigInstanceCreatorFactory instanceCreatorFactory;
  private final Map<Class<? extends ParsedConfig>, ConfigInstanceCreator> instanceCreators;

  private final EventConfigInitializer eventConfigInitializer;

  @Inject
  private DefaultConfigGenerator(
      ConfigStorageProvider storageProvider,
      Parser objectReferenceParser,
      Factory configFactory,
      ConfigImplementer configImplementer,
      EventBus eventBus,
      ConfigDiscoveredEvent.Factory eventFactory,
      ConfigAnnotationCollector annotationCollector,
      ImplementationGenerator implementationGenerator,
      ConfigInstanceCreatorFactory instanceCreatorFactory,
      EventConfigInitializer eventConfigInitializer) {
    this.storageProvider = storageProvider;
    this.objectReferenceParser = objectReferenceParser;
    this.configFactory = configFactory;
    this.configImplementer = configImplementer;
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
    this.annotationCollector = annotationCollector;
    this.implementationGenerator = implementationGenerator;
    this.instanceCreatorFactory = instanceCreatorFactory;
    this.eventConfigInitializer = eventConfigInitializer;

    this.instanceCreators = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<? extends ParsedConfig> generateConfigImplementation(CtClass configInterface)
      throws NotFoundException, CannotCompileException, IOException, ReflectiveOperationException {
    GeneratingConfig config = this.configFactory.create(configInterface);

    CtClass implementation = this.implementationGenerator.implementConfig(configInterface, config);
    if (implementation == null) {
      return null;
    }

    // add the ParsedConfig interface to the base implementation (annotated with @Config)
    this.configImplementer.implementParsedConfig(implementation, config);

    // add the SubConfig interface to every other implementation
    for (CtClass generated : config.getGeneratedImplementations()) {
      if (!implementation.getName().equals(generated.getName())) {
        this.configImplementer.implementSubConfig(generated, config);
      }

      // Class might get abstract while generating, but abstract methods will be removed again
      generated.setModifiers(generated.getModifiers() & ~Modifier.ABSTRACT);
    }

    // load the interfaces so that the transformers will be applied
    for (CtClass implementedInterface : config.getImplementedInterfaces()) {
      this.loadAllClasses(implementedInterface);
    }

    // define the newly generated classes
    for (CtClass generated : config.getGeneratedImplementations()) {
      byte[] bytes = generated.toBytecode();
      LaunchController.getInstance().getRootLoader()
          .commonDefineClass(generated.getName(), bytes, 0, bytes.length, null);
    }

    Class<? extends ParsedConfig> result = (Class<? extends ParsedConfig>)
        super.getClass().getClassLoader().loadClass(implementation.getName());
    Class<? extends ParsedConfig> resolvedConfigInterface = (Class<? extends ParsedConfig>)
        super.getClass().getClassLoader().loadClass(configInterface.getName());

    this.registerInstanceCreator(resolvedConfigInterface, config);

    return result;
  }

  private void loadAllClasses(CtClass implemented)
      throws ClassNotFoundException, NotFoundException {
    // load all inner classes first so that they don't get frozen when loading the outer classes
    for (CtClass declared : implemented.getDeclaredClasses()) {
      this.loadAllClasses(declared);
    }

    super.getClass().getClassLoader().loadClass(implemented.getName());
  }

  private void registerInstanceCreator(
      Class<? extends ParsedConfig> configInterface,
      GeneratingConfig config)
      throws ReflectiveOperationException, CannotCompileException, IOException {
    ConfigInstanceCreator creator = this.instanceCreatorFactory.newCreator(this, config);

    CtClass implementation = config.getGeneratedImplementation(configInterface.getName());
    Class<? extends ParsedConfig> resolvedImplementation = (Class<? extends ParsedConfig>)
        super.getClass().getClassLoader().loadClass(implementation.getName());

    this.instanceCreators.put(configInterface, creator);
    this.instanceCreators.put(resolvedImplementation, creator);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ParsedConfig createConfigInstance(
      Class<? extends ParsedConfig> configClass, boolean storeContent) {
    ConfigInstanceCreator creator = this.instanceCreators.get(configClass);
    ParsedConfig config = creator != null ? creator.newInstance(storeContent) : null;
    if (config != null && storeContent && config.shouldWriteDefaults()) {
      this.storageProvider.write(config);
    }
    return config;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<Class<? extends ParsedConfig>> getDiscoveredConfigs() {
    return Collections.unmodifiableCollection(this.instanceCreators.keySet());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindConfig(GeneratingConfig generatingConfig, ParsedConfig config)
      throws IllegalStateException {
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

    this.eventBus.fireEvent(this.eventFactory.create(config), Subscribe.Phase.POST);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initConfig(ParsedConfig config) {
    boolean storeContent = config.shouldStoreContent();
    config.setStoreContent(false);

    for (ConfigObjectReference reference : config.getConfigReferences()) {
      Object defaultValue = reference.getDefaultValue();
      if (defaultValue != null) {
        reference.setValue(defaultValue);
      }
    }

    this.storageProvider.read(config);

    config.setStoreContent(storeContent);
    config.setInitialized();
  }
}
