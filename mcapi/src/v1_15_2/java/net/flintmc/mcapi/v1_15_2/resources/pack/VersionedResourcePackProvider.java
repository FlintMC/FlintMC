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

package net.flintmc.mcapi.v1_15_2.resources.pack;

import com.google.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.resources.pack.ResourcePack;
import net.flintmc.mcapi.resources.pack.ResourcePackProvider;
import net.minecraft.client.Minecraft;

/**
 * 1.15.2 implementation of the {@link ResourcePackProvider}
 */
@Singleton
@Implement(ResourcePackProvider.class)
public class VersionedResourcePackProvider implements ResourcePackProvider {

  /**
   * {@inheritDoc}
   */
  public List<ResourcePack> getEnabled() {
    return Minecraft.getInstance().getResourcePackList().getEnabledPacks().stream()
        .map(VersionedResourcePack::new)
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ResourcePack> getAvailable() {
    return Minecraft.getInstance().getResourcePackList().getAllPacks().stream()
        .map(VersionedResourcePack::new)
        .collect(Collectors.toList());
  }
}
