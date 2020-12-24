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

package net.flintmc.framework.inject.assisted.data;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Represents a data interface about how a method in an assisted inject factory will be assisted.
 */
public interface AssistedMethod {

  /**
   * Retrieves teh factory method that is being assisted.
   *
   * @return The factory method that is being assisted.
   */
  Method getFactoryMethod();

  /**
   * Retrieves the implementation type that will be created when the method is used.
   *
   * @return The implementation type that will be created when the method is used.
   */
  TypeLiteral<?> getImplementationType();

  /**
   * Retrieves teh constructor that will be used to construct instances of the implementation.
   *
   * @return The constructor that will be used to construct instances of the implementation.
   */
  Constructor<?> getImplementationConstructor();

  /**
   * Retrieves all non-assisted dependencies required to construct and inject the implementation.
   *
   * @return All non-assisted dependencies required to construct and inject the implementation.
   */
  Set<Dependency<?>> getDependencies();
}
