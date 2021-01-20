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

package net.flintmc.mcapi.v1_16_5.player.overlay;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.player.overlay.TabOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

/**
 * 1.16.5 implementation of {@link TabOverlay}
 */
@Singleton
@Implement(value = TabOverlay.class, version = "1.16.5")
public class VersionedTabOverlay implements TabOverlay {

  private final MinecraftComponentMapper minecraftComponentMapper;

  @Inject
  private VersionedTabOverlay(MinecraftComponentMapper minecraftComponentMapper) {
    this.minecraftComponentMapper = minecraftComponentMapper;
  }

  /**
   * Retrieves the header of this player.
   *
   * @return The header of this player
   */
  @Override
  public ChatComponent getHeader() {
    AccessibleTabOverlay accessibleTabOverlay =
        (AccessibleTabOverlay) Minecraft.getInstance().ingameGUI.getTabList();
    return this.minecraftComponentMapper.fromMinecraft(accessibleTabOverlay.getHeader());
  }

  /**
   * Updates the header of this player.
   *
   * @param header The new header content
   */
  @Override
  public void updateHeader(ChatComponent header) {
    Minecraft.getInstance()
        .ingameGUI
        .getTabList()
        .setFooter(
            header == null
                ? null
                : (ITextComponent) this.minecraftComponentMapper.toMinecraft(header));
  }

  /**
   * Retrieves the footer of this player.
   *
   * @return The footer of this player.
   */
  @Override
  public ChatComponent getFooter() {
    AccessibleTabOverlay accessibleTabOverlay =
        (AccessibleTabOverlay) Minecraft.getInstance().ingameGUI.getTabList();
    return this.minecraftComponentMapper.fromMinecraft(accessibleTabOverlay.getFooter());
  }

  /**
   * Updates the footer of this player.
   *
   * @param footer The new footer content
   */
  @Override
  public void updateFooter(ChatComponent footer) {
    Minecraft.getInstance()
        .ingameGUI
        .getTabList()
        .setFooter(
            footer == null
                ? null
                : (ITextComponent) this.minecraftComponentMapper.toMinecraft(footer));
  }
}
