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

package net.flintmc.mcapi.internal.settings.flint;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.mcapi.settings.flint.EnumFieldResolver;
import org.apache.logging.log4j.Logger;

@Singleton
@Implement(EnumFieldResolver.class)
public class DefaultEnumFieldResolver implements EnumFieldResolver {

  private final Logger logger;
  private final Map<Class<?>, Map<String, Field>> cachedFields;

  @Inject
  private DefaultEnumFieldResolver(@InjectLogger Logger logger) {
    this.logger = logger;
    this.cachedFields = new HashMap<>();
  }

  @Override
  public Field getEnumField(Enum<?> value) {
    return this.getEnumFields(value.getDeclaringClass()).get(value.name());
  }

  @Override
  public Map<String, Field> getEnumFields(Class<? extends Enum<?>> enumClass) {
    if (this.cachedFields.containsKey(enumClass)) {
      return this.cachedFields.get(enumClass);
    }

    Map<String, Field> fields = new HashMap<>();

    for (Enum<?> constant : enumClass.getEnumConstants()) {
      try {
        fields.put(constant.name(), enumClass.getDeclaredField(constant.name()));
      } catch (NoSuchFieldException e) {
        this.logger.error(
            "Failed to find enum constant field in " + enumClass.getName() + ": " + constant.name(),
            e);
      }
    }

    Map<String, Field> unmodifiable = Collections.unmodifiableMap(fields);
    this.cachedFields.put(enumClass, unmodifiable);
    return unmodifiable;
  }
}
