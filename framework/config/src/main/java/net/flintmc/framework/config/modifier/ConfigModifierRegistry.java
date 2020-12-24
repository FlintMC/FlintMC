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
import net.flintmc.framework.config.generator.method.ConfigObjectReference;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Registry containing all {@link ConfigModificationHandler}s for the modification of annotations inside a {@link
 * Config}.
 *
 * @see ConfigModificationHandler
 */
public interface ConfigModifierRegistry {

  /**
   * Retrieves a collection of all handlers that are registered in this registry.
   *
   * @return The non-null immutable collection of all handlers in this registry
   */
  Collection<ConfigModificationHandler> getHandlers();

  /**
   * Modifies the given annotation with the appropriate handlers in this registry.
   *
   * @param reference  The non-null reference where this annotation is located
   * @param annotation The non-null annotation to be modified
   * @param <A>        The type of the annotation to be modified
   * @return The non-null modified annotation, if there were no modifications, the {@code annotation} without any
   * modifications will be returned
   */
  <A extends Annotation> A modify(ConfigObjectReference reference, A annotation);

}
