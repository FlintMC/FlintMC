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

import com.google.inject.spi.BindingTargetVisitor;
import net.flintmc.framework.inject.assisted.factory.AssistedFactoryModuleBuilder;

/**
 * Represents a visitor for the assisted factories.
 *
 * <p>If your {@link BindingTargetVisitor} implements this interface, bindings created by using
 * {@link AssistedFactoryModuleBuilder} will be visited through this interface.
 *
 * @param <T> The type of the binding.
 * @param <V> Any type to be returned by the visit method. Use {@link Void} with return {@code null}
 *     if no return type is needed.
 */
public interface AssistedInjectTargetVisitor<T, V> extends BindingTargetVisitor<T, V> {

  /**
   * Visits an {@link AssistedInjectBinding} created through {@link AssistedFactoryModuleBuilder}.
   *
   * @param assistedInjectBinding The assisted inject binding to be visited.
   * @return Any type to be returned by the visit method. Use {@link Void} with return {@code null}
   *     if no return type is needed.
   */
  V visit(AssistedInjectBinding<? extends T> assistedInjectBinding);
}
