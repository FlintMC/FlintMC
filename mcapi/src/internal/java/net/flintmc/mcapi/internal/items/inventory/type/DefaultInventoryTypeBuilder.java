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

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.stereotype.NameSpacedKey;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.items.inventory.InventoryDimension;
import net.flintmc.mcapi.items.inventory.InventoryType;

@Implement(InventoryType.Builder.class)
public class DefaultInventoryTypeBuilder implements InventoryType.Builder {

  private final ComponentBuilder.Factory componentFactory;

  private NameSpacedKey registryName;
  private ChatComponent defaultTitle;
  private InventoryDimension defaultDimension;
  private boolean customizableDimensions;

  @Inject
  public DefaultInventoryTypeBuilder(ComponentBuilder.Factory componentFactory) {
    this.componentFactory = componentFactory;
  }

  @Override
  public InventoryType.Builder registryName(NameSpacedKey registryName) {
    this.registryName = registryName;
    return this;
  }

  @Override
  public InventoryType.Builder defaultTitle(ChatComponent defaultTitle) {
    this.defaultTitle = defaultTitle;
    return this;
  }

  @Override
  public InventoryType.Builder defaultDimension(InventoryDimension defaultDimension) {
    this.defaultDimension = defaultDimension;
    return this;
  }

  @Override
  public InventoryType.Builder customizableDimensions() {
    this.customizableDimensions = true;
    return this;
  }

  @Override
  public InventoryType build() {
    Preconditions.checkNotNull(this.registryName, "Missing registryName");
    Preconditions.checkNotNull(this.defaultDimension, "Missing dimension");

    if (this.defaultTitle == null) {
      this.defaultTitle =
          this.componentFactory.translation().translationKey(this.registryName.getKey()).build();
    }

    return new DefaultInventoryType(
        this.registryName, this.defaultTitle, this.defaultDimension, this.customizableDimensions);
  }
}
