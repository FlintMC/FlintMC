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

package net.flintmc.framework.config.generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;

/**
 * This class collects annotations on specific methods and classes.
 */
public interface ConfigAnnotationCollector {

  /**
   * Finds the last annotation in any of the given methods or their declaring interfaces.
   *
   * @param methods        The non-null array of methods to search for
   * @param annotationType The non-null type of the annotation
   * @param <A>            The annotation which should be searched
   * @return The discovered annotation or {@code null}, if no method/class associated with this
   * reference is annotated with it.
   * @see ConfigObjectReference#findLastAnnotation(Class)
   */
  <A extends Annotation> A findLastAnnotation(Method[] methods, Class<? extends A> annotationType);

  /**
   * Finds all annotations in any of the given methods or their declaring interfaces.
   *
   * @return The non-null collection of all annotations on the associated methods and interfaces
   * @see ConfigObjectReference#findAllAnnotations()
   */
  Collection<Annotation> findAllAnnotations(Method[] methods);

  /**
   * Finds all annotations of the given type on the given class (not on methods/fields) and the
   * super classes and interfaces recursively.
   *
   * @param clazz          The non-null class to search annotations on
   * @param annotationType The non-null type of annotations to search for
   * @param <A>            The type of annotations to search for
   * @return The new non-null collection with all annotations on the given class from the given type
   */
  <A extends Annotation> Collection<A> getAllAnnotations(Class<?> clazz, Class<A> annotationType);
}
