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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.annotation.ExcludeStorage;
import net.flintmc.framework.config.annotation.IncludeStorage;
import net.flintmc.framework.config.defval.mapper.DefaultAnnotationMapperRegistry;
import net.flintmc.framework.config.generator.ConfigAnnotationCollector;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.internal.generator.method.reference.invoker.ReferenceInvocationGenerator;
import net.flintmc.framework.config.internal.generator.method.reference.invoker.ReferenceInvoker;
import net.flintmc.framework.config.storage.ConfigStorage;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.PrimitiveTypeLoader;

@Implement(ConfigObjectReference.class)
public class DefaultConfigObjectReference implements ConfigObjectReference {

  private final ParsedConfig config;

  private final ConfigAnnotationCollector annotationCollector;
  private final String key;
  private final String[] pathKeys;
  private final CtMethod[] correspondingCtMethods;
  private final Map<Class<? extends Annotation>, Annotation> lastAnnotations;
  private final Type serializedType;

  private final Object defaultValue;

  private final Class<?> configBaseClass;
  private final ReferenceInvoker invoker;
  private final Class<?> declaringClass;

  private Method[] correspondingMethods;
  private Collection<Annotation> allAnnotations;

  @AssistedInject
  private DefaultConfigObjectReference(
      ConfigAnnotationCollector annotationCollector,
      ReferenceInvocationGenerator invocationGenerator,
      DefaultAnnotationMapperRegistry defaultAnnotationMapperRegistry,
      @Assisted GeneratingConfig generatingConfig,
      @Assisted ParsedConfig config,
      @Assisted("pathKeys") String[] pathKeys,
      @Assisted("path") CtMethod[] path,
      @Assisted("correspondingMethods") CtMethod[] correspondingCtMethods,
      @Assisted("getter") CtMethod getter,
      @Assisted("setter") CtMethod setter,
      @Assisted("declaringClass") String declaringClass,
      @Assisted("serializedType") Type serializedType)
      throws ReflectiveOperationException, CannotCompileException, NotFoundException, IOException {
    this.config = config;
    this.annotationCollector = annotationCollector;
    this.pathKeys = pathKeys;
    this.key = String.join(".", pathKeys);
    this.correspondingCtMethods = correspondingCtMethods;
    this.serializedType = serializedType;

    ClassLoader classLoader = super.getClass().getClassLoader();
    this.configBaseClass = classLoader.loadClass(generatingConfig.getBaseClass().getName());
    this.declaringClass = classLoader.loadClass(declaringClass);
    this.invoker = invocationGenerator.generateInvoker(
        generatingConfig, this.getLastName(), path, getter, setter);
    this.invoker.setReference(this.config, this);

    this.lastAnnotations = new HashMap<>();

    this.defaultValue = this.findDefaultValue(defaultAnnotationMapperRegistry);
  }

  private Object findDefaultValue(DefaultAnnotationMapperRegistry defaultAnnotationMapperRegistry) {
    Object result = null;
    for (Class<? extends Annotation> annotationType :
        defaultAnnotationMapperRegistry.getAnnotationTypes()) {
      Annotation annotation = this.findLastAnnotation(annotationType);
      if (annotation != null) {
        result = defaultAnnotationMapperRegistry.getDefaultValue(this, annotation);
        break;
      }
    }

    Type type = this.getSerializedType();
    if (type instanceof Class<?>
        && Number.class.isAssignableFrom((Class<?>) type)
        && result instanceof Number) {
      // map e.g. doubles that could potentially be integers
      Number number = (Number) result;

      if (type.equals(Byte.class) || type.equals(byte.class)) {
        return number.byteValue();
      } else if (type.equals(Short.class) || type.equals(short.class)) {
        return number.shortValue();
      } else if (type.equals(Integer.class) || type.equals(int.class)) {
        return number.intValue();
      } else if (type.equals(Long.class) || type.equals(long.class)) {
        return number.longValue();
      } else if (type.equals(Double.class) || type.equals(double.class)) {
        return number.doubleValue();
      } else if (type.equals(Float.class) || type.equals(float.class)) {
        return number.floatValue();
      }
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getConfigBaseClass() {
    return this.configBaseClass;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getKey() {
    return this.key;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLastName() {
    return this.pathKeys[this.pathKeys.length - 1];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getPathKeys() {
    return this.pathKeys;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getDeclaringClass() {
    return this.declaringClass;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ParsedConfig getConfig() {
    return this.config;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Type getSerializedType() {
    return this.serializedType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <A extends Annotation> A findLastAnnotation(Class<? extends A> annotationType) {
    if (this.lastAnnotations.containsKey(annotationType)) {
      return (A) this.lastAnnotations.get(annotationType);
    }

    this.mapCorrespondingMethods();

    A annotation =
        this.annotationCollector.findLastAnnotation(this.correspondingMethods, annotationType);

    this.lastAnnotations.put(annotationType, annotation);

    return annotation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<Annotation> findAllAnnotations() {
    if (this.allAnnotations != null) {
      return this.allAnnotations;
    }

    this.mapCorrespondingMethods();

    Collection<Annotation> annotations =
        this.annotationCollector.findAllAnnotations(this.correspondingMethods);

    return this.allAnnotations = annotations;
  }

  private void mapCorrespondingMethods() {
    if (this.correspondingMethods == null) {
      try {
        this.correspondingMethods = this.mapMethods(this.correspondingCtMethods);
      } catch (ClassNotFoundException | NotFoundException | NoSuchMethodException e) {
        throw new RuntimeException(
            "Failed to map the corresponding CtMethods of the reference for '"
                + this.key
                + "' to java reflect methods",
            e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean appliesTo(ConfigStorage storage) {
    String name = storage.getName();

    IncludeStorage annotation = this.findLastAnnotation(IncludeStorage.class);
    ExcludeStorage excludeAnnotation = this.findLastAnnotation(ExcludeStorage.class);
    String[] included = annotation != null ? annotation.value() : new String[0];
    String[] excluded = excludeAnnotation != null ? excludeAnnotation.value() : new String[0];

    if (excluded.length != 0) {
      for (String s : excluded) {
        if (s.equals(name)) {
          return false;
        }
      }

      return true;
    }

    if (included.length != 0) {
      for (String s : included) {
        if (s.equals(name)) {
          return true;
        }
      }
      return false;
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getDefaultValue() {
    return this.defaultValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getValue() {
    return this.invoker.getValue(this.config);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValue(Object value) {
    this.invoker.setValue(this.config, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void copyTo(ParsedConfig config) {
    for (ConfigObjectReference dstReference : config.getConfigReferences()) {
      if (Arrays.equals(this.pathKeys, dstReference.getPathKeys())) {
        dstReference.setValue(this.getValue());
        break;
      }
    }
  }

  private Method[] mapMethods(CtMethod[] ctMethods)
      throws ClassNotFoundException, NotFoundException, NoSuchMethodException {
    Method[] methods = new Method[ctMethods.length];

    for (int i = 0; i < ctMethods.length; i++) {
      methods[i] = this.mapMethod(ctMethods[i]);
    }

    return methods;
  }

  private Method mapMethod(CtMethod ctMethod)
      throws ClassNotFoundException, NotFoundException, NoSuchMethodException {
    ClassLoader classLoader = super.getClass().getClassLoader();
    Class<?> declaringClass = classLoader.loadClass(ctMethod.getDeclaringClass().getName());

    CtClass[] ctParameters = ctMethod.getParameterTypes();
    Class<?>[] parameters = new Class[ctParameters.length];

    for (int j = 0; j < ctParameters.length; j++) {
      String name = ctParameters[j].getName();
      parameters[j] = PrimitiveTypeLoader.loadClass(classLoader, name);
    }

    return declaringClass.getMethod(ctMethod.getName(), parameters);
  }

  @Override
  public String toString() {
    return "DefaultConfigObjectReference("
        + this.declaringClass.getName()
        + " -> "
        + this.key
        + ")";
  }
}
