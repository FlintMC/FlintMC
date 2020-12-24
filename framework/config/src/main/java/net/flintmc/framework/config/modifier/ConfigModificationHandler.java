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

package net.flintmc.framework.config.modifier;

import net.flintmc.framework.config.annotation.Config;

import java.lang.annotation.Annotation;

/**
 * Handler for the modification of annotations in a {@link Config}. To register one, {@link AnnotationModifier} may be
 * used.
 * <p>
 * This can be used to dynamically set values on an annotation of a config value, especially useful for the Settings
 * module.
 *
 * @see AnnotationModifier
 */
public interface ConfigModificationHandler {

  /**
   * Modifies the given annotation, the exact process depends on the implementation.
   *
   * @param annotation The non-null annotation to be modified
   * @return The modified/new annotation, {@code null} if no modification should be applied
   */
  Annotation modify(Annotation annotation);

}
