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

package net.flintmc.mcapi.internal.items.inventory.type;

import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.items.inventory.InventoryDimension;
import net.flintmc.mcapi.items.inventory.InventoryType;

public class DefaultInventoryType implements InventoryType {

  private final NameSpacedKey registryName;
  private final ChatComponent defaultTitle;
  private final InventoryDimension defaultDimension;
  private final boolean customizableDimensions;

  public DefaultInventoryType(
      NameSpacedKey registryName,
      ChatComponent defaultTitle,
      InventoryDimension defaultDimension,
      boolean customizableDimensions) {
    this.registryName = registryName;
    this.defaultTitle = defaultTitle;
    this.defaultDimension = defaultDimension;
    this.customizableDimensions = customizableDimensions;
  }

  @Override
  public NameSpacedKey getRegistryName() {
    return this.registryName;
  }

  @Override
  public ChatComponent getDefaultTitle() {
    return this.defaultTitle;
  }

  @Override
  public InventoryDimension getDefaultDimension() {
    return this.defaultDimension;
  }

  @Override
  public boolean isCustomizableDimensions() {
    return this.customizableDimensions;
  }
}
