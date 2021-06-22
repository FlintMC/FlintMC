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

import com.google.common.base.Defaults;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.CtPrimitiveType;
import javassist.Modifier;
import javassist.NotFoundException;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.generator.method.ConfigMethodInfo;
import net.flintmc.framework.config.internal.generator.method.ConfigMethodGenerationUtils;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.util.commons.javassist.PrimitiveTypeLoader;

public class ConfigMultiGetterSetter implements ConfigMethod {

  public static final String ALL_PREFIX = "All";

  private final ConfigMethodGenerationUtils generationUtils;
  private final ConfigMethodInfo info;
  private final CtClass keyType;
  private final CtClass valueType;

  @AssistedInject
  private ConfigMultiGetterSetter(
      ConfigMethodGenerationUtils generationUtils,
      @Assisted ConfigMethodInfo info,
      @Assisted("keyType") CtClass keyType,
      @Assisted("valueType") CtClass valueType) {
    this.generationUtils = generationUtils;
    this.info = info;
    this.keyType = keyType;
    this.valueType = valueType;
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
    return "get" + ALL_PREFIX + this.info.getConfigName();
  }

  private String getSingleGetterName() {
    String prefix =
        this.valueType.equals(CtClass.booleanType)
            || this.valueType.getName().equals(Boolean.class.getName())
            ? "is"
            : "get";
    return prefix + this.info.getConfigName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSetterName() {
    return "set" + ALL_PREFIX + this.info.getConfigName();
  }

  private String getSingleSetterName() {
    return "set" + this.info.getConfigName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getMethodNames() {
    return new String[]{
        this.getGetterName(),
        this.getSingleGetterName(),
        this.getSetterName(),
        this.getSingleSetterName()
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CtClass[] getTypes() {
    return new CtClass[]{this.keyType, this.info.getStoredType(), this.valueType};
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Type getSerializedType() {
    return TypeToken.getParameterized(
        Map.class,
        this.generationUtils.loadImplementationOrDefault(this.info, this.keyType),
        this.generationUtils.loadImplementationOrDefault(this.info, this.valueType))
        .getType();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void generateMethods(CtClass target) throws CannotCompileException, NotFoundException {
    CtField field = this.generationUtils
        .generateOrGetField(this.info, target, "new java.util.HashMap()");

    if (!this.generationUtils.hasMethod(target, this.getGetterName())) {
      this.insertGetAll(target, field);
    }

    if (!this.generationUtils.hasMethod(target, this.getSingleGetterName())) {
      this.insertGetter(target, field);
    }

    if (!this.generationUtils.hasMethod(target, this.getSetterName())) {
      this.insertSetAll(target, field);
    }

    if (!this.generationUtils.hasMethod(target, this.getSingleSetterName())) {
      this.insertSetter(target, field);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void implementExistingMethods(CtClass target)
      throws CannotCompileException, NotFoundException {
    // generate the getter/setter for all values automatically if the key is an enum
    if (this.keyType.isEnum()) {
      if (!this.generationUtils.hasMethod(target, this.getGetterName())) {
        this.insertEnumGetAll(target);
      }

      if (!this.generationUtils.hasMethod(target, this.getSetterName())) {
        this.insertEnumSetAll(target);
      }
    } else {
      this.generationUtils.insertSaveConfig(
          this.info, this.getGetterName(), target.getDeclaredMethod(this.getSetterName()));
    }

    this.generationUtils.insertSaveConfig(
        this.info, this.getGetterName(), target.getDeclaredMethod(this.getSingleSetterName()));

    // validate if the methods exist or throw NotFoundException otherwise
    target.getDeclaredMethod(this.getGetterName());

    this.info.addedInterfaceMethods();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addInterfaceMethods(CtClass target) throws CannotCompileException {
    if (this.keyType.isEnum()) {
      if (!this.generationUtils.hasMethod(target, this.getGetterName())) {
        target.addMethod(CtNewMethod.make("java.util.Map " + this.getGetterName() + "();", target));
      }

      if (!this.generationUtils.hasMethod(target, this.getSetterName())) {
        target.addMethod(
            CtNewMethod.make("void " + this.getSetterName() + "(java.util.Map arg);", target));
      }
    }

    this.info.addedInterfaceMethods();
  }

  private void insertEnumGetAll(CtClass target) throws CannotCompileException {
    String valueFormat = "%s";
    if (this.valueType.isPrimitive()) {
      valueFormat = ((CtPrimitiveType) this.valueType).getWrapperName() + ".valueOf(%s)";
    }

    StringBuilder operations = new StringBuilder();
    for (CtField field : this.keyType.getFields()) {
      if (Modifier.isStatic(field.getModifiers())) {
        String constant = this.keyType.getName() + "." + field.getName();
        operations
            .append("map.put(")
            .append(constant)
            .append(", ")
            .append(
                String.format(
                    valueFormat, "this." + this.getSingleGetterName() + "(" + constant + ")"))
            .append(");");
      }
    }

    target.addMethod(CtNewMethod.make(
        "public java.util.Map "
            + this.getGetterName()
            + "() {"
            + "java.util.Map map = new java.util.HashMap();"
            + operations
            + "return map;"
            + "}",
        target));
  }

  private void insertEnumSetAll(CtClass target) throws CannotCompileException, NotFoundException {
    StringBuilder body = new StringBuilder();
    for (CtField field : this.keyType.getFields()) {
      if (Modifier.isStatic(field.getModifiers())) {
        String constant = this.keyType.getName() + "." + field.getName();

        String valueName =
            this.valueType instanceof CtPrimitiveType
                ? ((CtPrimitiveType) this.valueType).getWrapperName()
                : this.valueType.getName();
        String getter = "((" + valueName + ") map.get(" + constant + "))";

        body
            .append("if (map.containsKey(")
            .append(constant)
            .append(")) {")
            .append("this.")
            .append(this.getSingleSetterName())
            .append("(")
            .append(constant)
            .append(", ")
            .append(PrimitiveTypeLoader.asPrimitiveSource(this.valueType, getter))
            .append(");")
            .append("}");
      }
    }

    CtMethod method = CtNewMethod.make(
        "public void " + this.getSetterName() + "(java.util.Map map) {" + body + "}", target);
    this.generationUtils.insertSaveConfig(this.info, this.getGetterName(), method);
    target.addMethod(method);
  }

  private void insertGetAll(CtClass target, CtField field) throws CannotCompileException {
    target.addMethod(CtNewMethod.getter(this.getGetterName(), field));
  }

  private void insertSetAll(CtClass target, CtField field)
      throws CannotCompileException, NotFoundException {
    CtMethod method = CtNewMethod.make(
        "public void "
            + this.getSetterName()
            + "(java.util.Map map) { this."
            + field.getName()
            + " = map; }",
        target);
    this.generationUtils.insertSaveConfig(this.info, this.getGetterName(), method);
    target.addMethod(method);
  }

  private void insertSetter(CtClass target, CtField field)
      throws CannotCompileException, NotFoundException {
    String value = PrimitiveTypeLoader.asWrappedPrimitiveSource(this.valueType, "value");

    CtMethod method = CtNewMethod.make(
        String.format("public void %s(%s key, %s value) { this.%s.put(key, %s); }",
            this.getSingleGetterName(),
            this.keyType.getName(),
            this.valueType.getName(),
            field.getName(),
            value), target);
    this.generationUtils.insertSaveConfig(this.info, this.getGetterName(), method);

    target.addMethod(method);
  }

  private void insertGetter(CtClass target, CtField field) throws CannotCompileException {
    String defaultValue;
    try {
      defaultValue =
          this.valueType.isPrimitive()
              ? String.valueOf(
              Defaults.defaultValue(
                  PrimitiveTypeLoader.loadClass(
                      super.getClass().getClassLoader(), this.valueType.getName())))
              : "null";
    } catch (ClassNotFoundException e) {
      defaultValue = "null";
    }

    String getter =
        PrimitiveTypeLoader.asWrappedPrimitiveSource(
            this.valueType,
            "this." + field.getName() + ".getOrDefault(input, " + defaultValue + ")");

    target.addMethod(
        CtNewMethod.make(
            String.format("public %s %s(%s input) { return (%s) %s; }",
                this.valueType.getName(),
                this.getSingleGetterName(),
                this.keyType.getName(),
                this.valueType.getName(),
                getter),
            target));
  }

  @AssistedFactory(ConfigMultiGetterSetter.class)
  public interface Factory {

    ConfigMultiGetterSetter create(
        @Assisted ConfigMethodInfo info,
        @Assisted("keyType") CtClass keyType,
        @Assisted("valueType") CtClass valueType);
  }
}
