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

package net.flintmc.mcapi.internal.render.image.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;
import net.flintmc.mcapi.render.image.ImageRenderer;
import net.flintmc.mcapi.render.image.util.PlayerHeadRenderer;

@Singleton
@Implement(PlayerHeadRenderer.class)
public class DefaultPlayerHeadRenderer implements PlayerHeadRenderer {

  private final ImageRenderer imageRenderer;

  @Inject
  private DefaultPlayerHeadRenderer(ImageRenderer imageRenderer) {
    this.imageRenderer = imageRenderer;
  }

  /** {@inheritDoc} */
  @Override
  public void drawPlayerHead(float x, float y, float size, NetworkPlayerInfo info) {
    this.imageRenderer.bindTexture(info.getSkinLocation());
    this.imageRenderer.drawPartImage(
        x, y, 8, 8, 8, null, 8, 8, 64, 64, size, size, 255, 255, 255, 255);
  }
}
