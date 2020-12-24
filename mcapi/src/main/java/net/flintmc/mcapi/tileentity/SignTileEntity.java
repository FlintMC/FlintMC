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

package net.flintmc.mcapi.tileentity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;

/**
 * Represents the Minecraft sign tile entity.
 */
public interface SignTileEntity extends TileEntity {

  /**
   * Retrieves the text of the sign in the specified line.
   *
   * @param line The line of the sign to get the text.
   * @return The text of the sign in the specified line or {@code null}.
   */
  ChatComponent getText(int line);

  /**
   * Changes the text at the given line.
   *
   * @param line      The line to change the text.
   * @param component The new text for the line.
   */
  void setText(int line, ChatComponent component);

  /**
   * A factory for the {@link SignTileEntity}.
   */
  @AssistedFactory(SignTileEntity.class)
  interface Factory {

    /**
     * Creates a new {@link SignTileEntity} with the given non-null Minecraft sign tile entity.
     *
     * @param signTileEntity The non-null Minecraft sign tile entity.
     * @return A created sign tile entity.
     */
    SignTileEntity create(@Assisted("tileEntity") Object signTileEntity);
  }
}
