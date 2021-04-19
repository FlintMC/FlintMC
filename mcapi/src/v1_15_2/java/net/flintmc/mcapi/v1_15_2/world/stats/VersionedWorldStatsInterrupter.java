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

package net.flintmc.mcapi.v1_15_2.world.stats;

import com.google.inject.Singleton;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.flintmc.transform.hook.HookResult;
import net.minecraft.client.Minecraft;

@Singleton
public class VersionedWorldStatsInterrupter {

  @Hook(
      className = "net.minecraft.client.gui.screen.StatsScreen",
      methodName = "init",
      executionTime = ExecutionTime.BEFORE)
  public HookResult interruptStatsInit() {
    // Fixes NPE if VersionedWorldStatsProvider is initialized while not connected to a server
    return Minecraft.getInstance().getConnection() == null ? HookResult.BREAK : HookResult.CONTINUE;
  }

}
