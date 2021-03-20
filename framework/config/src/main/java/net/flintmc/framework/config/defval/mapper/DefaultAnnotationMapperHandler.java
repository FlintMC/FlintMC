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

package net.flintmc.framework.config.defval.mapper;

import java.lang.annotation.Annotation;
import java.util.function.Supplier;
import net.flintmc.framework.config.defval.annotation.DefaultString;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.serialization.ConfigSerializationHandler;

/**
 * The handler for mapping an annotation (e.g. {@link DefaultString}) to an object which can be
 * serialized by a {@link ConfigSerializationHandler}.
 *
 * @param <A> The type of the annotation that can be mapped
 */
public interface DefaultAnnotationMapperHandler<A extends Annotation> {

  /**
   * Maps the given annotation to an object that can be serialized by a {@link
   * ConfigSerializationHandler}.
   *
   * @param reference  The non-null reference that contains the given annotation
   * @param annotation The non-null annotation to be mapped
   * @return The supplier for the mapped object from the annotation, can be {@code null}
   */
  Supplier<Object> getDefaultValue(ConfigObjectReference reference, A annotation);

}
