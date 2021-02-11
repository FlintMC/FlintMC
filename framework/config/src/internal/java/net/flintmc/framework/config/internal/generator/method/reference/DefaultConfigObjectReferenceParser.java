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

package net.flintmc.framework.config.internal.generator.method.reference;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.generator.method.ConfigMethodInfo;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.internal.generator.base.ImplementationGenerator;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.inject.implement.Implement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Singleton
@Implement(ConfigObjectReference.Parser.class)
public class DefaultConfigObjectReferenceParser implements ConfigObjectReference.Parser {

  private final ConfigObjectReference.Factory factory;
  private final ConfigSerializationService serializationService;
  private final ImplementationGenerator implementationGenerator;

  @Inject
  public DefaultConfigObjectReferenceParser(
      ConfigObjectReference.Factory factory,
      ConfigSerializationService serializationService,
      ImplementationGenerator implementationGenerator) {
    this.factory = factory;
    this.serializationService = serializationService;
    this.implementationGenerator = implementationGenerator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConfigObjectReference parse(
      GeneratingConfig generatingConfig, ParsedConfig config, ConfigMethod method) {
    ConfigMethodInfo info = method.getInfo();

    // keys in the config
    Collection<String> pathKeys = new ArrayList<>(Arrays.asList(info.getPathPrefix()));
    pathKeys.add(info.getConfigName());

    // all methods that need to be invoked to get to the actual getter/setter method
    CtMethod[] methodPath;
    CtClass declaringClass;
    // the class which contains the method.getMethodNames()
    CtClass mainClass;

    try {
      methodPath = this.asMethods(generatingConfig, info.getPathPrefix());
      declaringClass =
          methodPath.length == 0
              ? info.getDeclaringClass()
              : methodPath[methodPath.length - 1].getReturnType();
      mainClass =
          methodPath.length == 0
              ? generatingConfig.getBaseClass()
              : methodPath[methodPath.length - 1].getReturnType();
    } catch (NotFoundException e) {
      throw new RuntimeException(
          "Failed to generate the references for the config "
              + generatingConfig.getBaseClass().getName(),
          e);
    }

    // all methods that belong to this group (e.g. setNAME, getNAME, getNAMEAll)
    Collection<CtMethod> allMethods = new HashSet<>(Arrays.asList(methodPath));

    for (CtMethod ctMethod : mainClass.getMethods()) {
      for (String methodName : method.getMethodNames()) {
        if (ctMethod.getName().equals(methodName)) {
          allMethods.add(ctMethod);
        }
      }
    }

    CtClass declaringImplementation =
        generatingConfig.getGeneratedImplementation(declaringClass.getName(), declaringClass);

    CtMethod getter = this.getMethod(declaringImplementation, method.getGetterName());
    CtMethod setter = this.getMethod(declaringImplementation, method.getSetterName());

    if (getter == null || setter == null) {
      throw new IllegalArgumentException(
          "Setter/Getter for "
              + info.getConfigName()
              + " ("
              + method.getGetterName()
              + "/"
              + method.getSetterName()
              + ") not found in "
              + declaringImplementation.getName());
    }

    return this.factory.create(
        generatingConfig,
        config,
        pathKeys.toArray(new String[0]),
        methodPath,
        allMethods.toArray(new CtMethod[0]),
        getter,
        setter,
        declaringClass.getName(),
        method.getSerializedType());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<ConfigObjectReference> parseAll(
      GeneratingConfig generatingConfig, ParsedConfig config) {
    Collection<ConfigObjectReference> references = new HashSet<>();

    for (ConfigMethod method : generatingConfig.getAllMethods()) {
      ConfigMethodInfo info = method.getInfo();
      if (!info.getStoredType().isInterface()
          || this.serializationService.hasSerializer(info.getStoredType())) {
        references.add(this.parse(generatingConfig, config, method));
      }
    }

    return references;
  }

  private CtMethod[] asMethods(GeneratingConfig config, String[] pathPrefix)
      throws NotFoundException {
    List<CtMethod> methods = new ArrayList<>();
    CtClass currentClass = config.getGeneratedImplementation(config.getBaseClass().getName());

    for (String prefix : pathPrefix) {
      CtMethod currentMethod = this.getMethod(currentClass, "get" + prefix);
      if (currentMethod == null) {
        continue;
      }

      methods.add(currentMethod);
      currentClass =
          config.getGeneratedImplementation(
              currentMethod.getReturnType().getName(), currentMethod.getReturnType());

      if (currentClass.equals(CtClass.voidType)) {
        throw new IllegalArgumentException("Cannot have void as a return type of a getter");
      }
    }

    return methods.toArray(new CtMethod[0]);
  }

  private CtMethod getMethod(CtClass declaringClass, String name) {
    for (CtMethod method : declaringClass.getMethods()) {
      if (method.getName().equals(name)) {
        return method;
      }
    }
    return null;
  }
}
