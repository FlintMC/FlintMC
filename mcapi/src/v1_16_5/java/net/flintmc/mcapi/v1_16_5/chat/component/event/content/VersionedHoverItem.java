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

package net.flintmc.mcapi.v1_16_5.chat.component.event.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent.Action;
import net.flintmc.mcapi.chat.component.event.content.HoverItem;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;

public class VersionedHoverItem implements HoverItem {

  private final MinecraftItemMapper itemMapper;
  private final MinecraftComponentMapper componentMapper;
  private final ItemStack itemStack;

  private List<ChatComponent> cachedText;

  @AssistedInject
  private VersionedHoverItem(
      MinecraftItemMapper itemMapper,
      MinecraftComponentMapper componentMapper,
      @Assisted ItemStack itemStack) {
    this.itemMapper = itemMapper;
    this.componentMapper = componentMapper;
    this.itemStack = itemStack;
  }

  @Override
  public Action getAction() {
    return Action.SHOW_ITEM;
  }

  @Override
  public List<ChatComponent> getAsText() {
    if (this.cachedText != null) {
      return this.cachedText;
    }

    net.minecraft.item.ItemStack item =
        (net.minecraft.item.ItemStack) this.itemMapper.toMinecraft(this.itemStack);

    Minecraft minecraft = Minecraft.getInstance();
    List<ITextComponent> components = item.getTooltip(
        minecraft.player, minecraft.gameSettings.advancedItemTooltips
            ? ITooltipFlag.TooltipFlags.ADVANCED
            : ITooltipFlag.TooltipFlags.NORMAL);

    List<ChatComponent> mapped = new ArrayList<>(components.size());

    for (ITextComponent component : components) {
      mapped.add(this.componentMapper.fromMinecraft(component));
    }

    return this.cachedText = Collections.unmodifiableList(mapped);
  }

  @Override
  public ItemStack getItemStack() {
    return this.itemStack;
  }

  @AssistedFactory(VersionedHoverItem.class)
  interface VersionedFactory {

    VersionedHoverItem create(@Assisted ItemStack itemStack);
  }
}
