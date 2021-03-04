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

package net.flintmc.mcapi.v1_15_2.render.text;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.render.text.raw.FontRenderer;
import net.flintmc.mcapi.render.text.raw.StringAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.fonts.Font;

@Singleton
@Implement(value = FontRenderer.class)
public class VersionedFontRenderer implements FontRenderer {

  private final Minecraft minecraft;

  @Inject
  private VersionedFontRenderer() {
    this.minecraft = Minecraft.getInstance();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getStringWidth(String text) {
    return this.minecraft.fontRenderer.getStringWidth(text);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCharWidth(char c) {
    return this.minecraft.fontRenderer.getCharWidth(c);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getBoldCharWidth(char c) {
    if (c == ChatColor.PREFIX_CHAR) {
      return 0;
    }

    Font font = ((ShadowFontRenderer) this.minecraft.fontRenderer).getFont();
    return font.findGlyph(c).getAdvance(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void drawString(
      float x,
      float y,
      String text,
      int rgba,
      StringAlignment alignment,
      int maxLineLength,
      boolean shadow,
      float xFactor,
      float yFactor) {
    boolean scale = xFactor != 1 || yFactor != 1;
    if (scale) {
      RenderSystem.scalef(xFactor, yFactor, 1F);
    }

    switch (alignment) {
      case RIGHT:
        x -= this.getStringWidth(text);
        break;

      case CENTER:
        x -= (float) this.getStringWidth(text) / 2F;

      default:
      case LEFT:
        break;
    }

    if (maxLineLength != 0) {
      this.minecraft.fontRenderer.drawSplitString(text, (int) x, (int) y, maxLineLength, rgba);
    } else {
      if (shadow) {
        this.minecraft.fontRenderer.drawStringWithShadow(text, x, y, rgba);
      } else {
        this.minecraft.fontRenderer.drawString(text, x, y, rgba);
      }
    }

    if (scale) {
      RenderSystem.scalef(1F / xFactor, 1F / yFactor, 1F);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void drawString(
      Object matrixStack,
      float x,
      float y,
      String text,
      int rgba,
      StringAlignment alignment,
      int maxLineLength,
      boolean shadow,
      float xFactor,
      float yFactor) {
    this.drawString(x, y, text, rgba, alignment, maxLineLength, shadow, xFactor, yFactor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> listFormattedString(String text, int wrapWidth) {
    return this.minecraft.fontRenderer.listFormattedStringToWidth(text, wrapWidth);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String wrapFormattedString(String text, int wrapWidth) {
    return this.minecraft.fontRenderer.wrapFormattedStringToWidth(text, wrapWidth);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getStringWrappedHeight(String text, int wrapWidth) {
    return this.minecraft.fontRenderer.getWordWrappedHeight(text, wrapWidth);
  }
}
