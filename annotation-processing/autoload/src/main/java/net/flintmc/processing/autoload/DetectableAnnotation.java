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

package net.flintmc.processing.autoload;

import java.lang.annotation.*;

/**
 * Marks an annotation that can be discovered at runtime. Annotations that are annotated with this
 * will be parsed at compile time and written to a {@link DetectableAnnotationProvider} by the
 * autoload module.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface DetectableAnnotation {

  /**
   * Defines the meta data types that can be attached to the target annotation. In order to be
   * recognized every metadata annotation must be annotated with {@link DetectableAnnotation} too.
   */
  Class<? extends Annotation>[] metaData() default {};

  /**
   * @return if this annotation requires a parent. A parent is defined as an annotation that
   *     includes this annotation type in its {@link #metaData()}
   */
  boolean requiresParent() default false;
}
