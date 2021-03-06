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

package net.flintmc.framework.config.internal.generator.base;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.NotFoundException;
import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.framework.config.generator.ConfigImplementer;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.generator.method.ConfigMethodInfo;
import net.flintmc.framework.config.generator.method.ConfigMethodResolver;
import net.flintmc.framework.config.internal.generator.service.ConfigImplementationMapper;
import net.flintmc.framework.config.internal.transform.ConfigTransformer;
import net.flintmc.framework.config.internal.transform.PendingTransform;

@Singleton
public class ImplementationGenerator {

  private final ClassPool pool;
  private final AtomicInteger counter;
  private final Random random;
  private final ConfigMethodResolver methodResolver;

  private final ConfigTransformer transformer;
  private final ConfigImplementer implementer;
  private final ConfigImplementationMapper implementationMapper;

  @Inject
  private ImplementationGenerator(
      ClassPool pool,
      ConfigMethodResolver methodResolver,
      ConfigTransformer transformer,
      ConfigImplementer implementer,
      ConfigImplementationMapper implementationMapper) {
    this.implementer = implementer;
    this.implementationMapper = implementationMapper;

    this.pool = pool;
    this.counter = new AtomicInteger();
    this.random = new Random();
    this.methodResolver = methodResolver;
    this.transformer = transformer;
  }

  public CtClass implementConfig(CtClass type, GeneratingConfig config)
      throws NotFoundException, CannotCompileException {
    this.methodResolver.resolveMethods(config);

    for (ConfigMethod method : config.getAllMethods()) {
      ConfigMethodInfo info = method.getInfo();

      // transform all methods
      this.transformer.getPendingTransforms().add(new PendingTransform(method));

      CtClass declaring = info.getDeclaringClass();
      if (declaring.hasAnnotation(ImplementedConfig.class)) {
        continue;
      }

      // generate only the methods in the interface by the transformer (will be added to it below)
      info.requireNoImplementation();

      if (config.getGeneratedImplementation(declaring.getName()) != null) {
        continue;
      }
      CtClass implementation = this.generateImplementation(declaring);
      this.buildConfigField(config, implementation, declaring);
      config.bindGeneratedImplementation(declaring, implementation);
    }

    for (ConfigMethod method : config.getAllMethods()) {
      CtClass implementation =
          config.getGeneratedImplementation(method.getInfo().getDeclaringClass().getName());
      if (implementation == null) {
        // the interface is annotated with @Implemented and therefore the implementation already
        // exists
        continue;
      }

      method.generateMethods(implementation);
    }

    Collection<CtClass> implementations =
        config.getAllMethods().stream()
            .map(method -> config.getGeneratedImplementation(
                method.getInfo().getDeclaringClass().getName()))
            .filter(Objects::nonNull)
            // if null the interface is annotated with @ImplementedConfig and therefore the
            // implementation already exists
            .distinct()
            .collect(Collectors.toList());

    for (CtClass implementation : implementations) {
      this.buildConstructor(config, implementation);
    }
    for (CtClass implementation : implementations) {
      this.fillConstructor(config, implementation);
    }

    CtClass baseImplementation = config.getGeneratedImplementation(type.getName());

    // Map the implementations so that they are available in the further process of the
    // config generation
    config.getAllMethods().stream()
        .map(method -> method.getInfo().getDeclaringClass())
        .filter(ifc -> config.getGeneratedImplementation(ifc.getName()) == null)
        .distinct()
        .forEach(ifc -> {
          if (ifc.hasAnnotation(ImplementedConfig.class)) {
            CtClass implementation = this.implementationMapper
                .getImplementationMappings().get(ifc.getName());
            if (implementation == null) {
              throw new IllegalStateException(String.format(
                  "Config %s has @ImplementedConfig annotation, but no @ConfigImplementation class can be mapped to it",
                  ifc.getName()));
            }

            config.bindGeneratedImplementation(ifc, implementation);
          }
        });

    return baseImplementation;
  }

  private CtClass generateImplementation(CtClass type) {
    CtClass implementation =
        this.pool.makeClass(
            type.getSimpleName()
                + "_"
                + this.counter.getAndIncrement()
                + "_"
                + this.random.nextInt(Integer.MAX_VALUE));
    implementation.addInterface(type);

    return implementation;
  }

  private void buildConfigField(GeneratingConfig config, CtClass implementation, CtClass declaring)
      throws CannotCompileException {
    String baseClass = config.getBaseClass().getName();

    String baseField = "private final transient " + baseClass + " config";
    if (baseClass.equals(declaring.getName())) {
      baseField += " = this";
    }
    implementation.addField(CtField.make(baseField + ";", implementation));

    if (baseClass.equals(declaring.getName())) {
      this.implementer.preImplementParsedConfig(implementation, config);
    } else {
      this.implementer.preImplementSubConfig(implementation, config);
    }
  }

  private void buildConstructor(GeneratingConfig config, CtClass implementation)
      throws CannotCompileException, NotFoundException {
    String baseClass = config.getBaseClass().getName();
    boolean base = implementation.subtypeOf(config.getBaseClass());

    String params = !base ? baseClass + " type" : "";
    String bodyPrefix = !base ? "this.config = type;" : "";

    String src =
        "public " + implementation.getSimpleName() + '(' + params + "){" + bodyPrefix + "}";
    implementation.addConstructor(CtNewConstructor.make(src, implementation));
  }

  private void fillConstructor(GeneratingConfig config, CtClass implementation)
      throws NotFoundException, CannotCompileException {
    StringBuilder body = new StringBuilder();

    for (ConfigMethod method : config.getAllMethods()) {
      if (implementation.subtypeOf(method.getInfo().getDeclaringClass())) {
        body.append(method.getInfo().getFieldValuesCreator());
      }
    }

    if (body.length() == 0) {
      return;
    }
    implementation.getDeclaredConstructors()[0].insertAfter(body.toString());
  }
}
