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

package net.flintmc.util.session.v1_15_2;

import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.session.SessionService;
import net.flintmc.util.session.internal.MinecraftSessionUpdater;
import net.minecraft.client.Minecraft;

@Singleton
@Implement(value = MinecraftSessionUpdater.class, version = "1.15.2")
public class VersionedMinecraftSessionUpdater implements MinecraftSessionUpdater {
  @Override
  public void update(SessionService service) {
    if (service.getUniqueId() == null) {
      return;
    }

    String uuid = service.getUniqueId().toString().replace("-", "");
    String name = service.getUsername();
    String accessToken = service.getAccessToken();

    SessionRefreshableMinecraft minecraft = (SessionRefreshableMinecraft) Minecraft.getInstance();
    minecraft.setSession(
        new net.minecraft.util.Session(
            name, uuid, accessToken, net.minecraft.util.Session.Type.MOJANG.toString()));
  }
}
