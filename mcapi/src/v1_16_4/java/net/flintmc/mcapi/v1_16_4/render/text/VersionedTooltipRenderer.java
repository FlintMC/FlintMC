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

package net.flintmc.mcapi.v1_16_4.render.text;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.render.text.raw.FontRenderer;
import net.flintmc.mcapi.render.text.tooltip.TooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

@Singleton
@Implement(value = TooltipRenderer.class, version = "1.16.4")
public class VersionedTooltipRenderer implements TooltipRenderer {

  private final FontRenderer fontRenderer;
  private final Minecraft minecraft;
  private final Screen screen;

  @Inject
  private VersionedTooltipRenderer(FontRenderer fontRenderer) {
    this.fontRenderer = fontRenderer;
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
    this.renderTooltip(null, x, y, text);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void renderTooltip(Object matrixStack, float x, float y, String text) {
    int width = this.minecraft.getMainWindow().getScaledWidth();
    int height = this.minecraft.getMainWindow().getScaledHeight();
    List<String> lines = this.fontRenderer.listFormattedString(text, Math.min(width / 2, 200));

    List<ITextComponent> components =
        lines.stream()
            .map(ITextComponent::getTextComponentOrEmpty)
            .collect(Collectors.toCollection(CopyOnWriteArrayList::new));

    this.screen.resize(Minecraft.getInstance(), width, height);
    this.screen.renderTooltip(
        (MatrixStack) matrixStack,
        Lists.transform(components, ITextComponent::func_241878_f),
        (int) x,
        (int) y);
  }
}
