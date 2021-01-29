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

package net.flintmc.util.mappings.utils.line.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.processing.autoload.DetectableAnnotation;

/**
 * Marks a {@link LineMappingHandler} to be automatically registered in the {@link
 * LineMappingHandlerRegistry}. The annotated class must implement {@link LineMappingHandler}.
 *
 * @see LineMappingHandler
 */
@DetectableAnnotation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LineObfuscationMapper {

  /**
   * Retrieves the case-insensitive names that should identify the annotated handler in the {@link
   * LineMappingHandlerRegistry}. These names are unique per {@link LineMappingHandlerRegistry} and
   * won't be registered if they are specified multiple times.
   *
   * @return The array of case-insensitive names that should identify the annotated handler
   */
  String[] value();
}
