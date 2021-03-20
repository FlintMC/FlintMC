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

package net.flintmc.framework.inject.assisted;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.metaprogramming.DetectableAnnotation;

/**
 * Declares an <b>interface</b> as a factory base for a given class.
 *
 * <p>Factories can be used to instantiate classes which required parameters which can not be
 * injected directly. For that methods with the name {@code create} and a signature matching the
 * constructor of the implementation classes can be created, which then will be used to construct
 * the underlying implementations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DetectableAnnotation
public @interface AssistedFactory {

  /**
   * The class this assisted factory instantiates. May be an interface, in which case classes
   * implementing the interface are used for instantiation. The return types of the {@code create}
   * methods have to have a return type compatible with this type.
   *
   * @return The class this factory is able to instantiate
   */
  Class<?> value();
}
