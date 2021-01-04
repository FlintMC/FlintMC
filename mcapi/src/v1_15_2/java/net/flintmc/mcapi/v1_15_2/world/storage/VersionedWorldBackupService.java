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

package net.flintmc.mcapi.v1_15_2.world.storage;

import com.google.inject.Singleton;
import java.io.IOException;
import java.nio.file.Path;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.world.storage.service.WorldBackupService;
import net.minecraft.client.Minecraft;

@Singleton
@Implement(value = WorldBackupService.class, version = "1.15.2")
public class VersionedWorldBackupService implements WorldBackupService {

  /**
   * {@inheritDoc}
   */
  @Override
  public long createBackup(String fileName) throws IOException {
    return Minecraft.getInstance().getSaveLoader().createBackup(fileName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Path getBackupFolder() {
    return Minecraft.getInstance().getSaveLoader().getBackupsFolder();
  }
}
