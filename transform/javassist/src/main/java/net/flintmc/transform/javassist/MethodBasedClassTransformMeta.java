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

package net.flintmc.transform.javassist;

import javassist.CtClass;
import javassist.CtMethod;
import net.flintmc.metaprogramming.AnnotationMeta;
import net.flintmc.util.commons.resolve.NameResolver;

import java.util.Collection;
import java.util.function.Predicate;

public interface MethodBasedClassTransformMeta extends ClassTransformMeta {

  /**
   * Retrieves filters.
   *
   * @return Filters.
   */
  Collection<Predicate<CtClass>> getFilters();

  /**
   * Retrieves the transformer method.
   *
   * @return An transformer method.
   */
  CtMethod getTransformMethod();

  /**
   * Retrieves the transformer method.
   *
   * @return the transformer method
   */
  CtClass getTransformClass();

  /**
   * Retrieves the transformers instance
   *
   * @return the transformer instance
   */
  Object getTransformInstance();

  /**
   * Retrieves the annotation meta.
   *
   * @return the annotation meta.
   */
  AnnotationMeta<ClassTransform> getAnnotationMeta();

  /**
   * Retrieves the annotation.
   *
   * @return the annotation
   */
  ClassTransform getAnnotation();

  /**
   * Retrieves the name resolver.
   *
   * @return A name resolver.
   */
  NameResolver getClassNameResolver();

  //@AssistedFactory(MethodBasedClassTransformMeta.class)
  interface Factory {
    MethodBasedClassTransformMeta create(AnnotationMeta annotationMeta);
  }
}
