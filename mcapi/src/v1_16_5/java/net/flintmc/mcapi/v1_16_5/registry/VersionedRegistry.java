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

package net.flintmc.mcapi.v1_16_5.registry;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.registry.RegistryRegisterEvent;
import net.flintmc.mcapi.registry.RegistryRegisterEvent.Factory;
import net.flintmc.mcapi.resources.ResourceLocationProvider;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

@Singleton
public class VersionedRegistry {

  private final EventBus eventBus;
  private final ResourceLocationProvider locationProvider;
  private final RegistryRegisterEvent.Factory registryRegisterEventFactory;

  @Inject
  private VersionedRegistry(
      final EventBus eventBus,
      final ResourceLocationProvider locationProvider,
      final Factory registryRegisterEventFactory) {
    this.eventBus = eventBus;
    this.locationProvider = locationProvider;
    this.registryRegisterEventFactory = registryRegisterEventFactory;
  }

  @SuppressWarnings("rawtypes")
  @Hook(
      className = "net.minecraft.util.registry.Registry",
      methodName = "register",
      parameters = {
          @Type(typeName = "net.minecraft.util.registry.Registry"),
          @Type(typeName = "net.minecraft.util.ResourceLocation"),
          @Type(reference = Object.class)
      },
      executionTime = ExecutionTime.BEFORE
  )
  public void hookBeforeRegister(@Named("args") Object[] args) {
    Registry registry = (Registry) args[0];
    Object registryValue = args[2];

    RegistryKey minecraftRegistryKey = registry.getRegistryKey();

    if (minecraftRegistryKey == null) {
      return;
    }

    this.eventBus.fireEvent(
        this.registryRegisterEventFactory.create(
            this.locationProvider.fromMinecraft(minecraftRegistryKey.getLocation()),
            this.locationProvider.fromMinecraft(args[1]),
            registryValue),
        Phase.PRE);
  }

  @SuppressWarnings({"rawtypes", "RedundantExplicitVariableType"})
  @Hook(
      className = "net.minecraft.util.registry.Registry",
      methodName = "register",
      parameters = {
          @Type(typeName = "net.minecraft.util.registry.Registry"),
          @Type(reference = int.class),
          @Type(reference = String.class),
          @Type(reference = Object.class)
      },
      executionTime = ExecutionTime.BEFORE
  )
  public void hookBeforeRegisterWithId(@Named("args") Object[] args) {
    Registry registry = (Registry) args[0];
    int id = (int) args[1];
    String identifier = (String) args[2];
    Object registryValue = args[3];

    RegistryKey minecraftRegistryKey = registry.getRegistryKey();

    if (minecraftRegistryKey == null) {
      return;
    }

    this.eventBus.fireEvent(
        this.registryRegisterEventFactory.create(
            this.locationProvider.fromMinecraft(minecraftRegistryKey.getLocation()),
            this.locationProvider.fromMinecraft(new ResourceLocation(identifier)),
            id,
            registryValue),
        Phase.PRE);
  }


}
