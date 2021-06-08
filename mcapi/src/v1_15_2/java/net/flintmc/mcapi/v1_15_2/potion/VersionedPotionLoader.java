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

package net.flintmc.mcapi.v1_15_2.potion;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.event.subscribe.PreSubscribe;
import net.flintmc.mcapi.potion.PotionRegister;
import net.flintmc.mcapi.potion.mapper.PotionMapper;
import net.flintmc.mcapi.registry.RegistryRegisterEvent;
import net.flintmc.mcapi.resources.ResourceLocation;

@Singleton
public class VersionedPotionLoader {

  private final PotionRegister potionRegister;
  private final PotionMapper potionMapper;

  @Inject
  private VersionedPotionLoader(final PotionRegister potionRegister,
      final PotionMapper potionMapper) {
    this.potionRegister = potionRegister;
    this.potionMapper = potionMapper;
  }

  @PreSubscribe
  public void registerEffects(final RegistryRegisterEvent event) {
    ResourceLocation registryKeyLocation = event.getRegistryKeyLocation();

    if (registryKeyLocation.getPath().equals("mob_effect")) {
      this.potionRegister.addEffect(
          event.getRegistryValueLocation(),
          this.potionMapper.fromMinecraftEffect(event.getRegistryObject())
      );
    } else if (registryKeyLocation.getPath().equals("potion")) {
      this.potionRegister.addPotion(
          event.getRegistryValueLocation(),
          this.potionMapper.fromMinecraftPotion(event.getRegistryObject())
      );
    }
  }

}
