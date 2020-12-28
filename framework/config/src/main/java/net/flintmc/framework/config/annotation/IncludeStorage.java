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

package net.flintmc.framework.config.annotation;

import net.flintmc.framework.config.storage.ConfigStorage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Acts as a whitelist for {@link ConfigStorage}s inside a {@link Config}, more information can be found in the {@link
 * Config}.
 * <p>
 * The opposite of {@link ExcludeStorage} (exclude has a higher priority).
 *
 * @see Config
 * @see ExcludeStorage
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface IncludeStorage {

  /**
   * An array of storages that should be included, empty to include all storages. If the array is empty, it has the same
   * effect as if the annotation would just not be specified.
   * <p>
   * For example "local" as the value would specify that the value would be only stored in the default FlintMC storage.
   *
   * @return The non-null array of storages to be included
   */
  String[] value();

}
