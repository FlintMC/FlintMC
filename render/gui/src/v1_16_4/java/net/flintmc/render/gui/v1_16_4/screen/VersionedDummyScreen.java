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

package net.flintmc.render.gui.v1_16_4.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.flintmc.render.gui.screen.BuiltinScreenDisplayer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

/**
 * Dummy screen for 1.16.4 that renders nothing and only exists to display the mouse.
 *
 * @see BuiltinScreenDisplayer#displayMouse()
 */
public class VersionedDummyScreen extends Screen {

  protected VersionedDummyScreen() {
    super(new StringTextComponent(""));
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    // nothing to be rendered
  }

  @Override
  protected void init() {
    // nothing to be initialized
  }
}
