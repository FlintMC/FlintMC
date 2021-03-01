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

package net.flintmc.framework.config.internal.generator.method;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.event.ConfigValueUpdateEvent;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigMethodInfo;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.config.storage.ConfigStorageProvider;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.InjectedFieldBuilder;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.PrimitiveTypeLoader;
import net.flintmc.framework.stereotype.service.CtResolver;

@Singleton
public class ConfigMethodGenerationUtils {

  private static final String REFERENCE_CLASS = ConfigObjectReference.class.getName();

  private final CtClass eventClass;
  private final ConfigSerializationService serializationService;
  private final InjectedFieldBuilder.Factory fieldBuilderFactory;

  @Inject
  private ConfigMethodGenerationUtils(
      ClassPool pool,
      ConfigSerializationService serializationService,
      InjectedFieldBuilder.Factory fieldBuilderFactory) throws NotFoundException {
    this.eventClass = pool.get(Event.class.getName());
    this.serializationService = serializationService;
    this.fieldBuilderFactory = fieldBuilderFactory;
  }

  public Class<?> loadImplementationOrDefault(ConfigMethodInfo info, CtClass superClass) {
    CtClass implementation = info.getConfig().getGeneratedImplementation(superClass.getName());

    Class<?> primitiveType = PrimitiveTypeLoader.getWrappedClass(superClass.getName());
    if (primitiveType != null) { // primitive classes can't be loaded with the ClassLoader
      return primitiveType;
    }
    return CtResolver.get(implementation != null ? implementation : superClass);
  }

  public CtField generateOrGetField(ConfigMethodInfo info, CtClass target)
      throws CannotCompileException {
    return this.generateOrGetField(info, target, null);
  }

  public CtField generateOrGetField(ConfigMethodInfo info, CtClass target, String defaultValue)
      throws CannotCompileException {
    try {
      return target.getDeclaredField(info.getConfigName());
    } catch (NotFoundException ignored) {
    }

    String fieldValue = defaultValue;
    CtClass methodType = info.getStoredType();

    if (defaultValue == null
        && methodType.isInterface()
        && !this.serializationService.hasSerializer(methodType)) {
      CtClass implementation = info.getConfig().getGeneratedImplementation(methodType.getName());

      if (implementation != null) {
        fieldValue = "new " + implementation.getName() + "(this.config)";
      } else {
        fieldValue =
            InjectionHolder.class.getName()
                + ".getInjectedInstance("
                + methodType.getName()
                + ".class)";
      }
    }

    String fieldSrc =
        "private " + methodType.getName() + " " + info.getConfigName() + ";";

    CtField field = CtField.make(fieldSrc, target);
    target.addField(field);

    if (fieldValue != null) {
      info.initializeField(field.getName(), fieldValue);
    }

    return field;
  }

  public boolean hasMethod(CtClass type, String name) {
    try {
      type.getDeclaredMethod(name);
      return true;
    } catch (NotFoundException e) {
      return false;
    }
  }

  public void insertSaveConfig(ConfigMethodInfo info, String getterName, CtMethod method)
      throws CannotCompileException, NotFoundException {
    CtClass declaring = method.getDeclaringClass();

    CtClass baseClass = info.getConfig().getBaseClass();
    CtField field;
    if (declaring.subtypeOf(baseClass)) {
      try {
        field = declaring.getDeclaredField("config");
      } catch (NotFoundException exception) {
        field = CtField
            .make("private final transient " + baseClass.getName() + " config = this;", declaring);
        declaring.addField(field);
      }
    } else {
      field = this.fieldBuilderFactory.create()
          .inject(baseClass)
          .target(declaring)
          .fieldName("config")
          .notStatic()
          .generate();
    }

    CtField storageProvider = this.fieldBuilderFactory.create()
        .inject(ConfigStorageProvider.class)
        .target(declaring)
        .generate();

    CtField eventBus = this.fieldBuilderFactory.create()
        .inject(EventBus.class)
        .target(declaring)
        .generate();
    CtField eventFactory = this.fieldBuilderFactory.create()
        .inject(ConfigValueUpdateEvent.Factory.class)
        .target(declaring)
        .generate();

    CtField referenceField = this.getReferenceField(
        declaring, "configReference" + info.getConfigName());

    String fire = String.format(
        "%s.fireEvent(%%s, %s.%%s);",
        eventBus.getName(),
        Phase.class.getName());

    String event = String.format(
        "%s.create(this.%s, ($w) %%s)",
        eventFactory.getName(),
        referenceField.getName());

    String postEvent = String.format(event, "this." + getterName + "()");

    String preEvent;
    if (method.getParameterTypes().length == 1) {
      // Simple setters should be fired with the actual new value
      preEvent = String.format(event, "$1");
    } else {
      // The value of multi getters keeps being the same instance (one map) and
      // doesn't need to be the new value
      preEvent = postEvent;
    }
    String preFire = String.format(fire, preEvent, "PRE");

    if (method.getMethodInfo().getCodeAttribute() != null) {
      method.insertBefore(preFire);
    } else {
      method.setBody(preFire);
    }

    String writeSrc = String.format(
        "if (this.shouldStoreContent()) { %s.write((%s) this.%s); }",
        storageProvider.getName(),
        ParsedConfig.class.getName(),
        field.getName());

    String postFire = String.format(fire, postEvent, "POST");
    method.insertAfter(writeSrc + postFire);
  }

  private CtField getReferenceField(CtClass declaring, String name) throws CannotCompileException {
    try {
      return declaring.getDeclaredField(name);
    } catch (NotFoundException exception) {
      // public to be accessible from the ReferenceInvocationGenerator
      CtField field = CtField.make(
          String.format("public %s %s;", REFERENCE_CLASS, name), declaring);
      declaring.addField(field);
      return field;
    }
  }

}
