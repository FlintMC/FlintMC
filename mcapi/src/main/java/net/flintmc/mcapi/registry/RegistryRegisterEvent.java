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

package net.flintmc.mcapi.registry;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.resources.ResourceLocation;

/**
 * This event is only fired in the Pre phase when an object is registered for a registry. This event
 * cannot be <b>canceled</b>, as they could cause errors/bugs in the game.
 * <p>
 * <b>Note:</b> This is currently only fired for the default registry objects of Minecraft!
 */
@Subscribable(Phase.PRE)
public interface RegistryRegisterEvent extends Event {

  /**
   * Retrieves the {@link ResourceLocation} of the registry.
   *
   * @return The registry resource location.
   */
  ResourceLocation getRegistryKeyLocation();

  /**
   * Retrieves the {@link ResourceLocation} of the object registered for the registry.
   *
   * @return The resource location of the object.
   */
  ResourceLocation getRegistryValueLocation();

  /**
   * Retrieves the object registered for the registry.
   *
   * @return The registered object.
   */
  Object getRegistryObject();

  /**
   * Retrieves the identifier of the registered object.
   * <p>
   * <b>Note: </b> The identifier can be {@code -1}, which means that the registered object has not
   * an identifier.
   *
   * @return An identifier of the object or {@code -1}.
   */
  int getId();

  /**
   * A factory to create {@link RegistryRegisterEvent}'s.
   */
  @AssistedFactory(RegistryRegisterEvent.class)
  interface Factory {

    /**
     * Creates a new {@link RegistryRegisterEvent} with the given parameters.
     *
     * @param registryKeyLocation   The registry {@link ResourceLocation}.
     * @param registryValueLocation The {@link ResourceLocation} of the object to be registered.
     * @param registryObject        The object to be registered.
     * @return A created registry register event.
     */
    RegistryRegisterEvent create(
        @Assisted("registryKeyLocation") final ResourceLocation registryKeyLocation,
        @Assisted("registryValueLocation") final ResourceLocation registryValueLocation,
        @Assisted final Object registryObject);

    /**
     * Creates a new {@link RegistryRegisterEvent} with the given parameters.
     *
     * @param registryKeyLocation   The registry {@link ResourceLocation}.
     * @param registryValueLocation The {@link ResourceLocation} of the object to be registered.
     * @param id                    The identifier of the object to be registered.
     * @param registryObject        The object to be registered.
     * @return A created registry register event.
     */
    RegistryRegisterEvent create(
        @Assisted("registryKeyLocation") final ResourceLocation registryKeyLocation,
        @Assisted("registryValueLocation") final ResourceLocation registryValueLocation,
        @Assisted final int id,
        @Assisted final Object registryObject
    );
  }

}
