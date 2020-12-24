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

package net.flintmc.framework.inject.assisted.factory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class AssistedFactoryMethodHandle {

  private static final int MODIFIERS =
      Modifier.PRIVATE | Modifier.STATIC | Modifier.PUBLIC | Modifier.PROTECTED;
  private final Constructor<MethodHandles.Lookup> lookupConstructor;

  /** Constructs a new assisted factory method handle. */
  protected AssistedFactoryMethodHandle() {
    this.lookupConstructor = this.findConstructorHandlesLookup();
  }

  /**
   * Creates a {@link MethodHandle} with the given parameters.
   *
   * @param method The method to creates a constructor.
   * @param proxy The proxy of the method.
   * @return A created method handle or {@code null}.
   */
  public MethodHandle createMethodHandle(Method method, Object proxy) {
    if (this.lookupConstructor == null) {
      return null;
    }

    Class<?> declaringClass = method.getDeclaringClass();

    try {
      MethodHandles.Lookup lookup = this.lookupConstructor.newInstance(declaringClass, MODIFIERS);
      method.setAccessible(true);

      return lookup.unreflectSpecial(method, declaringClass).bindTo(proxy);
    } catch (ReflectiveOperationException exception) {
      throw new RuntimeException("Unable to access method: " + method, exception);
    }
  }

  /** @see MethodHandles.Lookup */
  private Constructor<MethodHandles.Lookup> findConstructorHandlesLookup() {
    try {
      Constructor<MethodHandles.Lookup> constructorLookup =
          MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
      constructorLookup.setAccessible(true);
      return constructorLookup;
    } catch (ReflectiveOperationException ignored) {
      return null;
    }
  }
}
