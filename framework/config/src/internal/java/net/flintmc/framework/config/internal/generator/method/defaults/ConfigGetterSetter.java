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

package net.flintmc.framework.config.internal.generator.method.defaults;

import java.lang.reflect.Type;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.generator.method.ConfigMethodInfo;
import net.flintmc.framework.config.internal.generator.method.ConfigMethodGenerationUtils;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.inject.assisted.AssistedInject;

public class ConfigGetterSetter implements ConfigMethod {

  private final ConfigMethodGenerationUtils generationUtils;
  private final ConfigSerializationService serializationService;
  private final ConfigMethodInfo info;

  @AssistedInject
  private ConfigGetterSetter(
      ConfigMethodGenerationUtils generationUtils,
      ConfigSerializationService serializationService,
      @Assisted ConfigMethodInfo info) {
    this.generationUtils = generationUtils;
    this.serializationService = serializationService;
    this.info = info;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConfigMethodInfo getInfo() {
    return this.info;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getGetterName() {
    CtClass type = this.info.getStoredType();

    String prefix =
        type.equals(CtClass.booleanType) || type.getName().equals(Boolean.class.getName())
            ? "is"
            : "get";
    return prefix + this.info.getConfigName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSetterName() {
    return "set" + this.info.getConfigName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getMethodNames() {
    return new String[]{this.getGetterName(), this.getSetterName()};
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CtClass[] getTypes() {
    return new CtClass[]{this.info.getStoredType()};
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Type getSerializedType() {
    return this.generationUtils.loadImplementationOrDefault(this.info, this.info.getStoredType());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void generateMethods(CtClass target) throws CannotCompileException, NotFoundException {
    if (!this.generationUtils.hasMethod(target, this.getGetterName())) {
      this.insertGetter(target);
    }

    if (!this.generationUtils.hasMethod(target, this.getSetterName())) {
      this.insertSetter(target);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void implementExistingMethods(CtClass target)
      throws CannotCompileException, NotFoundException {
    // validate if the methods exist or throw NotFoundException otherwise
    target.getDeclaredMethod(this.getGetterName());

    // add the write method to the setter
    try {
      this.generationUtils.insertSaveConfig(
          this.info, this.getGetterName(), target.getDeclaredMethod(this.getSetterName()));
    } catch (NotFoundException e) {
      CtClass methodType = this.info.getStoredType();
      if (!methodType.isInterface()
          && !this.serializationService.hasSerializer(methodType)) {
        // ignore if it is an interface that will never get overridden by anything
        throw e;
      }
    }

    this.info.implementedExistingMethods();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addInterfaceMethods(CtClass target) throws CannotCompileException {
    CtClass methodType = this.info.getStoredType();

    if (!this.generationUtils.hasMethod(target, this.getGetterName())) {
      target.addMethod(
          CtNewMethod.make(methodType.getName() + " " + this.getGetterName() + "();", target));
    }

    if (!this.generationUtils.hasMethod(target, this.getSetterName())) {
      target.addMethod(
          CtNewMethod.make(
              "void " + this.getSetterName() + "(" + methodType.getName() + " arg);", target));
    }

    this.info.addedInterfaceMethods();
  }

  private void insertGetter(CtClass target) throws CannotCompileException {
    target.addMethod(CtNewMethod.getter(
        this.getGetterName(), this.generationUtils.generateOrGetField(this.info, target)));
  }

  private void insertSetter(CtClass target) throws CannotCompileException, NotFoundException {
    CtMethod method = CtNewMethod.make(
        String.format("public void %s(%s arg) { this.%s = arg; }",
            this.getSetterName(), this.info.getStoredType().getName(), this.info.getConfigName()),
        target);

    this.generationUtils.insertSaveConfig(this.info, this.getGetterName(), method);
    target.addMethod(method);
  }

  @AssistedFactory(ConfigGetterSetter.class)
  public interface Factory {

    ConfigGetterSetter create(@Assisted ConfigMethodInfo info);
  }
}
