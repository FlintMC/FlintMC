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

package net.flintmc.framework.config.storage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.metaprogramming.DetectableAnnotation;

/**
 * Marks a storage to be automatically registered in the {@link ConfigStorageProvider}. If a storage
 * is manually registered, this annotation is still necessary to set the priority.
 *
 * <p>This annotation is to be used on classes that implement {@link ConfigStorage}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface StoragePriority {

  /**
   * Retrieves the priority of the underlying storage. The higher the value (the absolute minimum is
   * {@link Integer#MIN_VALUE}), the later the read method will be called by {@link
   * ConfigStorageProvider#read(ParsedConfig)} and therefore the lower the chance of the values that
   * have been read being overridden is.
   *
   * <p><br>
   * The "normal" priority is {@code 0}.
   *
   * @return The priority which can be any int
   */
  int value() default 0;
}
