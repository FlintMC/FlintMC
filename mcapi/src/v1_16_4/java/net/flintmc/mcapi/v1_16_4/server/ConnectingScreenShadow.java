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

package net.flintmc.mcapi.v1_16_4.server;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.network.NetworkManager;

/**
 * Shadow implementation of the {@link ConnectingScreen} in minecraft with a public getter to get
 * the network manager.
 */
@Shadow(value = "net.minecraft.client.gui.screen.ConnectingScreen", version = "1.16.4")
public interface ConnectingScreenShadow {

  /**
   * Retrieves the network manager of this screen which is used to connect with a server.
   *
   * @return The non-null network manager of this screen
   */
  @FieldGetter("networkManager")
  NetworkManager getNetworkManager();
}