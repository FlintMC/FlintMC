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

package net.flintmc.mcapi.v1_15_2.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.ConnectedServer;
import net.flintmc.mcapi.server.ServerAddress;
import net.flintmc.mcapi.server.ServerController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.client.gui.screen.DirtMessageScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.text.TranslationTextComponent;

@Singleton
@Implement(value = ServerController.class, version = "1.15.2")
public class VersionedServerController implements ServerController {

  private final ConnectedServer connectedServer;

  @Inject
  public VersionedServerController(ConnectedServer connectedServer) {
    this.connectedServer = connectedServer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isConnected() {
    return this.connectedServer.isConnected();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConnectedServer getConnectedServer() {
    return this.isConnected() ? this.connectedServer : null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disconnectFromServer() {
    if (Minecraft.getInstance().world != null) {
      // rendering has to be called from Minecraft's main thread
      Minecraft.getInstance().execute(this::disconnectNow);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void connectToServer(ServerAddress address) {
    // rendering has to be called from Minecraft's main thread
    Minecraft.getInstance()
        .execute(
            () -> {
              this.disconnectNow();

              Screen currentScreen = Minecraft.getInstance().currentScreen;
              Minecraft.getInstance()
                  .displayGuiScreen(
                      new ConnectingScreen(
                          currentScreen,
                          Minecraft.getInstance(),
                          address.getIP(),
                          address.getPort()));
            });
  }

  private void disconnectNow() {
    if (Minecraft.getInstance().world == null) {
      return;
    }

    boolean integrated = Minecraft.getInstance().isIntegratedServerRunning();
    boolean realms = Minecraft.getInstance().isConnectedToRealms();
    Minecraft.getInstance().world.sendQuittingDisconnectingPacket();

    if (integrated) {
      Minecraft.getInstance()
          .unloadWorld(new DirtMessageScreen(new TranslationTextComponent("menu.savingLevel")));
    } else {
      Minecraft.getInstance().unloadWorld();
    }

    if (integrated) {
      Minecraft.getInstance().displayGuiScreen(new MainMenuScreen());
    } else if (realms) {
      RealmsBridge bridge = new RealmsBridge();
      bridge.switchToRealms(new MainMenuScreen());
    } else {
      Minecraft.getInstance().displayGuiScreen(new MultiplayerScreen(new MainMenuScreen()));
    }
  }
}
