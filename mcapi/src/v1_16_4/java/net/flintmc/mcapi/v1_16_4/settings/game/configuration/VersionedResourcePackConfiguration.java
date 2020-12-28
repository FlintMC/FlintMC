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

package net.flintmc.mcapi.v1_16_4.settings.game.configuration;

import com.google.inject.Singleton;
import java.util.List;
import net.flintmc.framework.config.annotation.implemented.ConfigImplementation;
import net.flintmc.mcapi.settings.game.configuration.ResourcePackConfiguration;
import net.minecraft.client.Minecraft;

/** 1.16.4 implementation of {@link ResourcePackConfiguration}. */
@Singleton
@ConfigImplementation(value = ResourcePackConfiguration.class, version = "1.16.4")
public class VersionedResourcePackConfiguration implements ResourcePackConfiguration {

  /** {@inheritDoc} */
  @Override
  public List<String> getResourcePacks() {
    return Minecraft.getInstance().gameSettings.resourcePacks;
  }

  /** {@inheritDoc} */
  @Override
  public void setResourcePacks(List<String> resourcePacks) {
    Minecraft.getInstance().gameSettings.resourcePacks = resourcePacks;
    Minecraft.getInstance().gameSettings.saveOptions();
  }

  /** {@inheritDoc} */
  @Override
  public List<String> getIncompatibleResourcePacks() {
    return Minecraft.getInstance().gameSettings.incompatibleResourcePacks;
  }

  /** {@inheritDoc} */
  @Override
  public void setIncompatibleResourcePacks(List<String> incompatibleResourcePacks) {
    Minecraft.getInstance().gameSettings.incompatibleResourcePacks = incompatibleResourcePacks;
    Minecraft.getInstance().gameSettings.saveOptions();
  }
}
