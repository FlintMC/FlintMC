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
import java.util.List;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.render.text.tooltip.TooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

@Singleton
@Implement(value = TooltipRenderer.class)
public class VersionedTooltipRenderer implements TooltipRenderer {

  private final Minecraft minecraft;
  private final Screen screen;

  @Inject
  private VersionedTooltipRenderer() {
    this.minecraft = Minecraft.getInstance();
    this.screen = new Screen(new StringTextComponent("")) {
    };
    this.screen.init(minecraft, 0, 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void renderTooltip(float x, float y, String text) {
    int width = this.minecraft.getMainWindow().getScaledWidth();
    int height = this.minecraft.getMainWindow().getScaledHeight();
    List<String> lines =
        this.minecraft.fontRenderer.listFormattedStringToWidth(text, Math.min(width / 2, 200));

    this.screen.setSize(width, height);

    this.screen.renderTooltip(lines, (int) x, (int) y);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void renderTooltip(Object matrixStack, float x, float y, String text) {
    this.renderTooltip(x, y, text);
  }
}
