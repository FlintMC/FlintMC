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

import com.google.inject.Key;
import java.util.Collection;
import net.flintmc.framework.inject.assisted.factory.AssistedFactoryModuleBuilder;

/**
 * Represents a binding for a factory created by {@link AssistedFactoryModuleBuilder}.
 *
 * @param <T> The fully qualified type of the factory.
 */
public interface AssistedInjectBinding<T> {

  /**
   * Retrieves the {@link Key} for the factory binding.
   *
   * @return The {@link Key} for the factory binding.
   */
  Key<T> getKey();

  /**
   * Retrieves an {@link AssistedMethod} for each method in the factory.
   *
   * @return An {@link AssistedMethod} for each method in the factory.
   */
  Collection<AssistedMethod> getAssistedMethods();
}
