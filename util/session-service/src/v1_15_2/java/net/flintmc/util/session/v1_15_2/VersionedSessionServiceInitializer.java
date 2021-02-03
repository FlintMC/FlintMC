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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.util.UUIDTypeAdapter;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.mcapi.event.MinecraftInitializeEvent;
import net.flintmc.util.session.internal.SessionServiceUpdater;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

@Singleton
public class VersionedSessionServiceInitializer {

  private final SessionServiceUpdater updater;

  @Inject
  private VersionedSessionServiceInitializer(SessionServiceUpdater updater) {
    this.updater = updater;
  }

  @PostSubscribe(version = "1.15.2")
  public void initSession(MinecraftInitializeEvent event) {
    // load the session that has been given to the client by the launcher
    Session session = Minecraft.getInstance().getSession();
    if (session.getToken().equals("0")) {
      // not authenticated by the launcher
      return;
    }

    this.updater.updateAuthenticationContent(
        UUIDTypeAdapter.fromString(session.getPlayerID()),
        session.getUsername(),
        session.getToken());
  }
}
