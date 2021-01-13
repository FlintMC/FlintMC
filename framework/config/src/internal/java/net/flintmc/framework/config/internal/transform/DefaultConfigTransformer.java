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
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import net.flintmc.framework.config.annotation.implemented.ConfigImplementation;
import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.framework.config.generator.ConfigImplementer;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.storage.ConfigStorageProvider;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.javassist.ClassTransformMeta;
import net.flintmc.transform.launchplugin.LateInjectedTransformer;
import net.flintmc.transform.minecraft.MinecraftTransformer;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

@Singleton
@Service(
    value = ConfigImplementation.class,
    priority = 2 /* needs to be called after the ConfigGenerationService */)
@MinecraftTransformer
@Implement(ConfigTransformer.class)
public class DefaultConfigTransformer
    implements ConfigTransformer, LateInjectedTransformer, ServiceHandler<ConfigImplementation> {

  private final ClassPool pool;
  private final ConfigImplementer configImplementer;

  private final Collection<PendingTransform> pendingTransforms;
  private final Collection<TransformedConfigMeta> mappings;
  private final Map<String, String> launchArguments;

  @Inject
  private DefaultConfigTransformer(
      ClassPool pool,
      ConfigImplementer configImplementer,
      @Named("launchArguments") Map launchArguments) {
    this.pool = pool;
    this.configImplementer = configImplementer;
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
    String version = annotation.version();

    if (!annotation.value().isAnnotationPresent(ImplementedConfig.class)) {
      throw new ServiceNotFoundException(
          "Cannot use @ConfigImplementation annotation without @ImplementedConfig on its value() ["
              + implementation.getName()
              + "/"
              + annotation.value().getName()
              + "]");
    }

    if (!version.isEmpty() && !launchArguments.get("--game-version").equals(version)) {
      return;
    }

    TransformedConfigMeta configMeta = new TransformedConfigMeta(
        annotation.value(), implementation);
    this.mappings.add(configMeta);

    for (PendingTransform transform : this.pendingTransforms) {
      if (transform.getMethod().getDeclaringClass().getName()
          .equals(annotation.value().getName())) {
        transform.setConfigMeta(configMeta);
      }
    }
  }

  @Override
  public byte[] transform(String className, byte[] classData) throws ClassTransformException {
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

    if (!shouldTransform) {
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
    } catch (CannotCompileException | NotFoundException exception) {
      throw new ClassTransformException(
          "Failed to implement config methods: " + className, exception);
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
      CtClass declaring = method.getDeclaringClass();
      if (!implementation.getName()
          .equals(transform.getConfigMeta().getImplementationCtClass().getName())) {
        continue;
      }

      // bind the new config class from the new class pool above with
      // new methods from this transformer
      method.getConfig().bindGeneratedImplementation(declaring, implementation);

      if (!modified
          && implementation.isInterface()
          && implementation.getName().equals(method.getConfig().getBaseClass().getName())) {
        // add the ParsedConfig interface to the config interface so that guice will also proxy this
        // one and not only the config itself
        implementation.addInterface(this.pool.get(ParsedConfig.class.getName()));
      }

      if (!implementation.isInterface()) {
        if (!modified) {
          implementation.addField(
              CtField.make(
                  "private final transient "
                      + ConfigStorageProvider.class.getName()
                      + " configStorageProvider = "
                      + InjectionHolder.class.getName()
                      + ".getInjectedInstance("
                      + ConfigStorageProvider.class.getName()
                      + ".class);",
                  implementation));

          for (TransformedConfigMeta meta : this.mappings) {
            if (meta.getConfig() == null
                && meta.getSuperClass().getName().equals(declaring.getName())) {
              meta.setConfig(method.getConfig());
            }
          }
        }

        if (method.getDeclaringClass().getName().equals(method.getConfig().getBaseClass().getName())
            && Arrays.stream(implementation.getInterfaces())
            .noneMatch(iface -> iface.getName().equals(ParsedConfig.class.getName()))) {
          // only the base class annotated with @Config should have the ParsedConfig implementation
          this.configImplementer.implementParsedConfig(
              implementation, method.getConfig().getName());
        }
      }

      if (implementation.isInterface()) {
        method.addInterfaceMethods(implementation);
      } else {
        method.implementExistingMethods(implementation);
      }

      if (method.hasAddedInterfaceMethods() && method.hasImplementedExistingMethods()) {
        // everything done, remove the method
        this.pendingTransforms.remove(transform);
      }

      modified = true;
    }
  }
}
