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

package net.flintmc.framework.inject.method;

import javassist.CtMethod;

/**
 * This injector can be used to fire methods using Guice injection. For faster execution, an
 * interface needs to be created which will automatically be implemented and which then invokes the
 * specified method. The target method may NOT be static. For example this interface could look like
 * this:
 *
 * <pre>
 *   public interface MyMethodInjector {
 *
 *     void invoke(@Named("test") Object test, @Named("secondObject") Object secondObject);
 *
 *   }
 * </pre>
 *
 * <p>The name of the method doesn't matter and can be anything.
 *
 * <p>The parameters will be mapped to the target method by their type and annotation.
 *
 * <p>The return type can also be anything else than void.
 *
 * <p>Now the interface can automatically be implemented with {@link Factory#generate(CtMethod,
 * Class)}.
 *
 * @see Factory
 * @see MethodInjectionUtils
 */
public interface MethodInjector {

  /**
   * Retrieves the instance that is used for the invocation of the method.
   *
   * @return The non-null instance of this injector
   */
  Object getInstance();

  /**
   * Changes the instance that should be used for the invocation of the method.
   *
   * @param instance The instance that should be used or {@code null} so that the instance will be
   *     used from the Injector the next time it is used
   */
  void setInstance(Object instance);

  /** Factory for the {@link MethodInjector}. */
  interface Factory {

    /**
     * Generates a new {@link MethodInjector} implementing the given interface. If an injector for
     * the given method has already been created, it will be used from the cache. The given
     * interface must have ONE method.
     *
     * @param targetMethod The non-null method to be invoked by the new injector (needs to be
     *     non-static)
     * @param ifc The non-null interface to be implemented
     * @param <T> The type of the interface to be implemented
     * @return The new non-null class implementing the given interface and {@link MethodInjector}
     * @throws MethodInjectorGenerationException If an error occurred while generating the injector
     * @see MethodInjector
     */
    <T> T generate(CtMethod targetMethod, Class<T> ifc) throws MethodInjectorGenerationException;

    /**
     * Generates a new {@link MethodInjector} implementing the given interface. If an injector for
     * the given method has already been created, it will be used from the cache. The given
     * interface must have ONE method.
     *
     * @param instance The instance that should be used to invoke the given method, if {@code null}
     *     is provided, the instance from the Injector will be used.
     * @param targetMethod The non-null method to be invoked by the new injector (needs to be
     *     non-static)
     * @param ifc The non-null interface to be implemented
     * @param <T> The type of the interface to be implemented
     * @return The new non-null class implementing the given interface and {@link MethodInjector}
     * @throws MethodInjectorGenerationException If an error occurred while generating the injector
     * @see MethodInjector
     */
    <T> T generate(Object instance, CtMethod targetMethod, Class<T> ifc)
        throws MethodInjectorGenerationException;
  }
}
