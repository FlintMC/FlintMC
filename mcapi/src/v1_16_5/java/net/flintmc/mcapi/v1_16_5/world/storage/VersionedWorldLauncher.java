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

package net.flintmc.mcapi.v1_16_5.world.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.storage.WorldOverview;
import net.flintmc.mcapi.world.storage.service.WorldLauncher;
import net.minecraft.client.Minecraft;

/**
 * 1.16.5 implementation of the {@link WorldLauncher}.
 */
@Singleton
@Implement(value = WorldLauncher.class)
public class VersionedWorldLauncher implements WorldLauncher {

  @Inject
  private VersionedWorldLauncher() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void launchWorld(WorldOverview worldOverview) {
    this.launchWorld(worldOverview.getFileName(), worldOverview.getDisplayName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void launchWorld(String fileName, String displayName) {
    if (Minecraft.getInstance().getSaveLoader().canLoadWorld(fileName)) {
      Minecraft.getInstance().loadWorld(fileName);
    }
  }
}
