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

package net.flintmc.transform.javassist.internal;

import com.google.inject.Key;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.CtClassFilter;
import net.flintmc.transform.javassist.MethodBasedClassTransformMeta;
import net.flintmc.util.commons.resolve.NameResolver;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;

public class DefaultMethodBasedClassTransformMeta implements MethodBasedClassTransformMeta {

  private final ClassTransformContext.Factory classTransformContextFactory;
  private final Collection<Predicate<CtClass>> filters;
  private final ClassMappingProvider classMappingProvider;
  private final AnnotationMeta<ClassTransform> annotationMeta;
  private final Logger logger;
  private final String version;
  private final NameResolver classNameResolver;
  private Object transformInstance;


  public DefaultMethodBasedClassTransformMeta(
          ClassTransformContext.Factory classTransformContextFactory,
          ClassMappingProvider classMappingProvider,
          Logger logger,
          AnnotationMeta<ClassTransform> annotationMeta,
          Map<String, String> launchArguments) {
    this.classTransformContextFactory = classTransformContextFactory;
    this.logger = logger;
    this.version = launchArguments.get("--game-version");
    this.classMappingProvider = classMappingProvider;
    this.annotationMeta = annotationMeta;
    this.classNameResolver =
        InjectionHolder.getInjectedInstance(getAnnotation().classNameResolver());

    this.filters = this.createFilters();
  }

  private Collection<Predicate<CtClass>> createFilters() {
    Collection<Predicate<CtClass>> filters = new HashSet<>();
    for (AnnotationMeta<CtClassFilter> ctClassFilter :
        this.getAnnotationMeta().getMetaData(CtClassFilter.class)) {
      filters.add(
          ctClass -> {
            CtClassFilter classFilterAnnotation = ctClassFilter.getAnnotation();
            try {
              NameResolver classNameResolver =
                  InjectionHolder.getInjectedInstance(classFilterAnnotation.classNameResolver());

              return classFilterAnnotation
                  .value()
                  .test(ctClass, classNameResolver.resolve(classFilterAnnotation.className()));

            } catch (NotFoundException exception) {
              logger.error(
                  "Exception while discovering service: {}",
                  classFilterAnnotation.className(),
                  exception);
            }
            return false;
          });
    }
    return Collections.unmodifiableCollection(filters);
  }

  @Override
  public void execute(CtClass ctClass) throws ClassTransformException {
    try {
      CtResolver.get(this.getTransformMethod())
          .invoke(this.getTransformInstance(), this.classTransformContextFactory.create(ctClass));
    } catch (IllegalAccessException exception) {
      throw new ClassTransformException(
          "Unable to access method: " + this.getTransformMethod().getName(), exception);
    } catch (InvocationTargetException exception) {
      throw new ClassTransformException(
          this.getTransformMethod().getName() + " threw an exception", exception);
    }
  }

  @Override
  public boolean matches(CtClass ctClass) {
    if (ctClass.equals(this.getTransformClass())) {
      return false;
    }
    for (Predicate<CtClass> filter : this.getFilters()) {
      if (!filter.test(ctClass)) return false;
    }
    ClassMapping classMapping = classMappingProvider.get(ctClass.getName());

    ClassTransform annotation = this.getAnnotationMeta().getAnnotation();
    if (classMapping == null)
      classMapping =
          new ClassMapping(
              InjectionHolder.getInjectedInstance(
                  Key.get(boolean.class, Names.named("obfuscated"))),
              ctClass.getName(),
              ctClass.getName());

    for (String target : annotation.value()) {
      String resolve = this.getClassNameResolver().resolve(target);

      if (resolve != null) {
        target = resolve;
      }

      return (annotation.version().isEmpty() || annotation.version().equals(this.version))
          && ((target.isEmpty() || target.equals(classMapping.getDeobfuscatedName()))
              || target.equals(classMapping.getObfuscatedName()))
          && this.getFilters().stream()
              .allMatch(ctClassPredicate -> ctClassPredicate.test(ctClass));
    }
    return true;
  }

  @Override
  public Collection<Predicate<CtClass>> getFilters() {
    return this.filters;
  }

  @Override
  public CtMethod getTransformMethod() {
    return this.annotationMeta.getMethodIdentifier().getLocation();
  }

  @Override
  public CtClass getTransformClass() {
    return this.getTransformMethod().getDeclaringClass();
  }

  @Override
  public Object getTransformInstance() {
    if (this.transformInstance == null) {
      this.transformInstance =
          InjectionHolder.getInjectedInstance(CtResolver.get(getTransformClass()));
    }
    return this.transformInstance;
  }

  @Override
  public AnnotationMeta<ClassTransform> getAnnotationMeta() {
    return this.annotationMeta;
  }

  @Override
  public ClassTransform getAnnotation() {
    return this.getAnnotationMeta().getAnnotation();
  }

  @Override
  public NameResolver getClassNameResolver() {
    return this.classNameResolver;
  }

  @Override
  public int getPriority() {
    return this.getAnnotation().priority();
  }
}
