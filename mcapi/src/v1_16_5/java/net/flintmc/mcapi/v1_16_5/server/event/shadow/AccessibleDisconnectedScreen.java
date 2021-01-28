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

package net.flintmc.mcapi.v1_16_5.server.event.shadow;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.util.text.ITextComponent;

/**
 * Shadow implementation of the {@link net.minecraft.client.gui.screen.DisconnectedScreen} in
 * minecraft with a public getter to get the message component.
 */
@Shadow(value = "net.minecraft.client.gui.screen.DisconnectedScreen", version = "1.16.5")
public interface AccessibleDisconnectedScreen {

  /**
   * Retrieves the message that will be displayed in this screen.
   *
   * @return The non-null message of this screen
   */
  @FieldGetter("message")
  ITextComponent getMessage();

}
