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

package net.flintmc.framework.stereotype.service;

import net.flintmc.metaprogramming.AnnotationMeta;
import java.lang.annotation.Annotation;

public interface ServiceHandler<T extends Annotation> {

  /**
   * Discover a service.
   *
   * @param annotationMeta The meta of the discovered annotation.
   * @throws ServiceNotFoundException If the service could not be discovered.
   */
  void discover(AnnotationMeta<T> annotationMeta) throws ServiceNotFoundException;

  /**
   * Called after {@link #discover(AnnotationMeta)} has been called for every annotation available
   * for the annotation of this service.
   */
  default void postDiscover() {
  }
}
