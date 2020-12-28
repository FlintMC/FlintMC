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

package net.flintmc.util.commons.resolve;

import java.lang.annotation.Annotation;

/**
 * Generic interface to resolve annotations to values.
 *
 * @param <A> The type of the annotation to resolve
 * @param <R> The resolved type
 */
@FunctionalInterface
public interface AnnotationResolver<A extends Annotation, R> extends Resolver<A, R> {

  /**
   * Resolves the given annotation to a value.
   *
   * @param a The annotation to resolve
   * @return The resolved value
   */
  R resolve(A a);
}
