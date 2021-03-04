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

package net.flintmc.mcapi.v1_16_5.render.image.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.flintmc.mcapi.render.image.util.ItemRenderer;
import net.flintmc.mcapi.resources.pack.ResourcePackReloadEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

/**
 * {@inheritDoc}
 */
@Singleton
@Implement(value = ItemRenderer.class)
public class VersionedItemRenderer implements ItemRenderer {

  private final MinecraftItemMapper itemMapper;
  private final FontRenderer fontRenderer;
  private final net.minecraft.client.renderer.ItemRenderer itemRenderer;
  private boolean initialized;

  @Inject
  private VersionedItemRenderer(MinecraftItemMapper itemMapper) {
    this.itemMapper = itemMapper;
    this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    this.fontRenderer = Minecraft.getInstance().fontRenderer;
  }

  @PostSubscribe
  public void handleResourceReload(ResourcePackReloadEvent event) {
    this.initialized = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void drawItemStack(float x, float y, float scale, ItemStack item) {
    this.drawRawItemStack(x, y, scale, this.itemMapper.toMinecraft(item));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void drawRawItemStack(float x, float y, float scale, Object minecraftItem) {
    if (!this.initialized) {
      return;
    }

    if (!(minecraftItem instanceof net.minecraft.item.ItemStack)) {
      throw new IllegalArgumentException(
          "Provided object is not an " + net.minecraft.item.ItemStack.class);
    }
    net.minecraft.item.ItemStack item = (net.minecraft.item.ItemStack) minecraftItem;

    RenderSystem.disableDepthTest();
    RenderSystem.pushMatrix();

    if (scale != 1) {
      RenderSystem.scalef(scale, scale, 1F);
    }

    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.enableRescaleNormal();
    RenderSystem.glMultiTexCoord2f(33986, 240.0F, 240.0F);
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    RenderSystem.translatef(0.0F, 0.0F, 32.0F);
    this.itemRenderer.zLevel = 200.0F;
    this.itemRenderer.renderItemAndEffectIntoGUI(item, (int) x, (int) y);
    this.itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, item, (int) x, (int) y, null);
    this.itemRenderer.zLevel = 0.0F;

    RenderSystem.popMatrix();
    RenderSystem.enableDepthTest();
  }
}
