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

package net.flintmc.framework.config.internal.transform;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import net.flintmc.framework.config.annotation.implemented.ConfigImplementation;
import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.framework.config.generator.ConfigImplementer;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.generator.method.ConfigMethodInfo;
import net.flintmc.framework.config.internal.generator.base.DefaultConfigImplementer;
import net.flintmc.framework.config.internal.generator.service.ImplementedConfigService;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.launcher.classloading.common.CommonClassLoader;
import net.flintmc.metaprogramming.AnnotationMeta;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.launchplugin.LateInjectedTransformer;
import net.flintmc.transform.minecraft.MinecraftTransformer;

@Singleton
@Service(
    value = ConfigImplementation.class,
    priority = 2 /* needs to be called after the ConfigGenerationService */)
@MinecraftTransformer(implementations = false)
@Implement(ConfigTransformer.class)
public class DefaultConfigTransformer
    implements ConfigTransformer, LateInjectedTransformer, ServiceHandler<ConfigImplementation> {

  private static final String PARSED_CONFIG_CLASS = ParsedConfig.class.getName();

  private final ClassPool pool;
  private final ConfigImplementer configImplementer;
  private final ImplementedConfigService implementedService;

  private final Collection<PendingTransform> pendingTransforms;
  private final Collection<TransformedConfigMeta> mappings;
  private final Map<String, String> launchArguments;

  @Inject
  private DefaultConfigTransformer(
      ClassPool pool,
      DefaultConfigImplementer configImplementer,
      ImplementedConfigService implementedService,
      @Named("launchArguments") Map launchArguments) {
    this.pool = pool;
    this.configImplementer = configImplementer;
    this.implementedService = implementedService;
    this.launchArguments = launchArguments;

    this.pendingTransforms = new HashSet<>();
    this.mappings = new HashSet<>();
  }

  @Override
  public Collection<TransformedConfigMeta> getMappings() {
    return this.mappings;
  }

  @Override
  public Collection<PendingTransform> getPendingTransforms() {
    return this.pendingTransforms;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(AnnotationMeta<ConfigImplementation> meta) throws ServiceNotFoundException {
    // implement the configs that are annotated with ImplementedConfig
    CtClass implementation = (CtClass) meta.getIdentifier().getLocation();
    ConfigImplementation annotation = meta.getAnnotation();

    if (!annotation.value().isAnnotationPresent(ImplementedConfig.class)) {
      throw new ServiceNotFoundException(
          "Cannot use @ConfigImplementation annotation without @ImplementedConfig on its value() ["
              + implementation.getName()
              + "/"
              + annotation.value().getName()
              + "]");
    }

    if (!meta.isApplicableForVersion(launchArguments.get("--game-version"))) {
      return;
    }

    TransformedConfigMeta configMeta =
        new TransformedConfigMeta(annotation.value(), implementation);
    this.mappings.add(configMeta);

    for (PendingTransform transform : this.pendingTransforms) {
      if (transform.getMethod().getInfo().getDeclaringClass().getName()
          .equals(annotation.value().getName())) {
        transform.setConfigMeta(configMeta);
      }
    }
  }

  @Override
  public byte[] transform(String className, CommonClassLoader classLoader, byte[] classData)
      throws ClassTransformException {
    // implement methods in classes of the config (including the class annotated with @Config) that
    // are also half-generated

    boolean shouldTransform = false;

    for (PendingTransform transform : this.pendingTransforms) {
      if (transform.getConfigMeta() != null &&
          transform.getConfigMeta().getImplementationCtClass().getName().equals(className)) {
        shouldTransform = true;
        break;
      }
    }

    boolean configInterface = this.implementedService.getConfigInterfaces().contains(className);

    if (!shouldTransform && !configInterface) {
      return classData;
    }

    CtClass transforming;

    try {
      transforming =
          this.pool.makeClass(
              new ClassFile(new DataInputStream(new ByteArrayInputStream(classData))), true);
    } catch (IOException exception) {
      throw new ClassTransformException("unable to read class", exception);
    }

    try {
      this.implementMethods(transforming);

      if (!transforming.isInterface()) {
        // Class might get abstract while transforming, but abstract methods will be removed again
        transforming.setModifiers(transforming.getModifiers() & ~Modifier.ABSTRACT);
      }
    } catch (CannotCompileException | NotFoundException exception) {
      throw new ClassTransformException(
          "Failed to implement config methods: " + className, exception);
    }

    if (configInterface) {
      try {
        transforming.addInterface(this.pool.get(PARSED_CONFIG_CLASS));
      } catch (NotFoundException exception) {
        throw new ClassTransformException("ParsedConfig not found in class pool", exception);
      }
    }

    try {
      return transforming.toBytecode();
    } catch (IOException | CannotCompileException exception) {
      // Basically unreachable.
      throw new ClassTransformException(
          "Unable to write class bytecode to byte array: " + className, exception);
    }
  }

  private void implementMethods(CtClass implementation)
      throws CannotCompileException, NotFoundException {
    if (this.pendingTransforms.isEmpty()) {
      return;
    }

    boolean modified = false;

    Collection<PendingTransform> copy = new HashSet<>(this.pendingTransforms);
    for (PendingTransform transform : copy) {
      if (transform.getConfigMeta() == null) {
        continue;
      }

      ConfigMethod method = transform.getMethod();
      ConfigMethodInfo info = method.getInfo();
      GeneratingConfig generatingConfig = info.getConfig();
      CtClass declaring = info.getDeclaringClass();
      if (!implementation.getName()
          .equals(transform.getConfigMeta().getImplementationCtClass().getName())) {
        continue;
      }

      // bind the new config class from the new class pool above with
      // new methods from this transformer
      generatingConfig.bindGeneratedImplementation(declaring, implementation);

      if (!implementation.isInterface()) {
        if (!modified) {
          for (TransformedConfigMeta meta : this.mappings) {
            if (meta.getConfig() != null) {
              continue;
            }

            if (meta.getSuperClass().getName().equals(declaring.getName())
                || meta.getImplementationCtClass().getName().equals(declaring.getName())) {
              meta.setConfig(generatingConfig);
            }
          }
        }

        boolean baseClass = info.getDeclaringClass().getName()
            .equals(generatingConfig.getBaseClass().getName());

        if (baseClass) {
          this.configImplementer.preImplementParsedConfig(implementation, generatingConfig);
        } else {
          this.configImplementer.preImplementSubConfig(implementation, generatingConfig);
        }

        if (implementation.isInterface()) {
          method.addInterfaceMethods(implementation);
        } else {
          method.implementExistingMethods(implementation);
        }

        if (baseClass) {
          // only the base class annotated with @Config should have the ParsedConfig implementation
          this.configImplementer.implementParsedConfig(
              implementation, generatingConfig);
        } else {
          // sub classes should implement the SubConfig interface
          this.configImplementer.implementSubConfig(implementation, generatingConfig);
        }
      }

      if (info.hasAddedInterfaceMethods() && info.hasImplementedExistingMethods()) {
        // everything done, remove the method
        this.pendingTransforms.remove(transform);
      }

      modified = true;
    }
  }
}
