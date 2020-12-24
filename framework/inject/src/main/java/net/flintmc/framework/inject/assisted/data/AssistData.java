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

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Dependency;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import net.flintmc.framework.inject.assisted.thread.ThreadLocalProvider;

/** The object stores important information for assisted methods. */
public class AssistData implements AssistedMethod {

  private final Constructor<?> constructor;
  private final Key<?> returnType;
  private final ImmutableList<Key<?>> parameterTypes;
  private final TypeLiteral<?> implementationType;
  private final Set<Dependency<?>> dependencies;
  private final Method factoryMethod;
  private final boolean optimized;
  private final List<ThreadLocalProvider> providers;
  private Binding<?> cachedBinding;

  /**
   * Constructs a new {@link AssistData} with the given parameters.
   *
   * @param constructor The constructor of the assisted data.
   * @param returnType The return type of the assisted data.
   * @param parameterTypes The parameter types of the assisted data.
   * @param implementationType The implementation type of the assisted data.
   * @param factoryMethod The factory method of the assisted data.
   * @param dependencies The dependencies of the assisted ata.
   * @param optimized {@code true} if the assisted data is optimized, otherwise {@code false}.
   * @param providers A collection with all providers of the assisted data.
   */
  public AssistData(
      Constructor<?> constructor,
      Key<?> returnType,
      ImmutableList<Key<?>> parameterTypes,
      TypeLiteral<?> implementationType,
      Method factoryMethod,
      Set<Dependency<?>> dependencies,
      boolean optimized,
      List<ThreadLocalProvider> providers) {
    this.constructor = constructor;
    this.returnType = returnType;
    this.parameterTypes = parameterTypes;
    this.implementationType = implementationType;
    this.factoryMethod = factoryMethod;
    this.dependencies = dependencies;
    this.optimized = optimized;
    this.providers = providers;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(getClass())
        .add("constructor", constructor)
        .add("return type", returnType)
        .add("parameter type", parameterTypes)
        .add("implementation type", implementationType)
        .add("dependencies", dependencies)
        .add("factory method", factoryMethod)
        .add("optimized", optimized)
        .add("providers", providers)
        .add("cached binding", cachedBinding)
        .toString();
  }

  /** {@inheritDoc} */
  @Override
  public Set<Dependency<?>> getDependencies() {
    return dependencies;
  }

  /** {@inheritDoc} */
  @Override
  public Method getFactoryMethod() {
    return factoryMethod;
  }

  /** {@inheritDoc} */
  @Override
  public Constructor<?> getImplementationConstructor() {
    return constructor;
  }

  /** {@inheritDoc} */
  @Override
  public TypeLiteral<?> getImplementationType() {
    return implementationType;
  }

  /**
   * Retrieves the constructor of the assist data.
   *
   * @return The constructor.
   */
  public Constructor<?> getConstructor() {
    return constructor;
  }

  /**
   * Retrieves the return type of this assist data.
   *
   * @return The return type as a {@link Key}.
   */
  public Key<?> getReturnType() {
    return returnType;
  }

  /**
   * Retrieves an immutable list with all parameter types.
   *
   * @return An immutable list with all parameter types.
   */
  public ImmutableList<Key<?>> getParameterTypes() {
    return parameterTypes;
  }

  /**
   * Whether the assist data is optimized.
   *
   * @return {@code true} if the assist data is optimized, otherwise {@code false}.
   */
  public boolean isOptimized() {
    return optimized;
  }

  /**
   * Retrieves a list with all {@link ThreadLocalProvider}'s.
   *
   * @return A list with all {@link ThreadLocalProvider}'s of this assist data.
   */
  public List<ThreadLocalProvider> getProviders() {
    return providers;
  }

  /**
   * Retrieves the cached binding of the assist data.
   *
   * @return The assist data cached binding.
   */
  public Binding<?> getCachedBinding() {
    return cachedBinding;
  }

  /**
   * Changes the cached binding of the assist data.
   *
   * @param cachedBinding The new cached binding.
   */
  public void setCachedBinding(Binding<?> cachedBinding) {
    this.cachedBinding = cachedBinding;
  }
}
