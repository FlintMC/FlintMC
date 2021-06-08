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

package net.flintmc.mcapi.internal.registry;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.registry.RegistryRegisterEvent;
import net.flintmc.mcapi.resources.ResourceLocation;

@Implement(RegistryRegisterEvent.class)
public class DefaultRegistryRegisterEvent implements RegistryRegisterEvent {

  private final ResourceLocation registryKeyLocation;
  private final ResourceLocation registryValueLocation;
  private final Object value;
  private final int id;

  @AssistedInject
  private DefaultRegistryRegisterEvent(
      @Assisted("registryKeyLocation") final ResourceLocation registryKeyLocation,
      @Assisted("registryValueLocation") final ResourceLocation registryValueLocation,
      @Assisted final Object value) {
    this(registryKeyLocation, registryValueLocation, value, -1);
  }

  @AssistedInject
  private DefaultRegistryRegisterEvent(
      @Assisted("registryKeyLocation") final ResourceLocation registryKeyLocation,
      @Assisted("registryValueLocation") final ResourceLocation registryValueLocation,
      @Assisted final Object value,
      @Assisted final int id) {
    this.registryKeyLocation = registryKeyLocation;
    this.registryValueLocation = registryValueLocation;
    this.value = value;
    this.id = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getRegistryKeyLocation() {
    return this.registryKeyLocation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getRegistryValueLocation() {
    return this.registryValueLocation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getRegistryObject() {
    return this.value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getId() {
    return this.id;
  }
}
