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

package net.flintmc.mcapi.settings.flint;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Resolver to map enum constants to their {@link Field}.
 */
public interface EnumFieldResolver {

  /**
   * Retrieves the field that belongs to the given enum constant.
   *
   * @param value The non-null enum constant
   * @return The non-null field to the given constant
   */
  Field getEnumField(Enum<?> value);

  /**
   * Retrieves all fields that belong to enum constants in the given enum class with the key being
   * the {@link Enum#name() name of the constant} and value the field that belongs to the enum
   * constant.
   *
   * @param enumClass The non-null enum class
   * @return The non-null map with all fields to the given constant
   */
  Map<String, Field> getEnumFields(Class<? extends Enum<?>> enumClass);
}
