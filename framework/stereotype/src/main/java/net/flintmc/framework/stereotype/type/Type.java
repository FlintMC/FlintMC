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

package net.flintmc.framework.stereotype.type;

import net.flintmc.util.commons.resolve.AnnotationResolver;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a {@link Class}. Used mostly for bytecode manipulation to not be forced to provide
 * direct class references. Only one of the methods {@link Type#typeName()} or {@link
 * Type#reference()} should be used. If both are provided, {@link Type#typeNameResolver()} should
 * handle it.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Type {

  /** @return direct class reference */
  Class<?> reference() default TypeDummy.class;

  /** @return Class by name, resolved and eventually modified by {@link Type#typeNameResolver()} */
  String typeName() default "";

  /** @return The type name resolver for this {@link Type} */
  Class<? extends AnnotationResolver<Type, String>> typeNameResolver() default
      DefaultTypeNameResolver.class;

  final class TypeDummy {
    private TypeDummy() {}
  }
}
