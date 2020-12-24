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

package net.flintmc.render.vbo;

/** Lists some predefined generic vertex attributes. */
public enum VertexAttributes implements VertexAttribute {
  /** Represents a generic 3D position. */
  POSITION3F(3),
  /** Represents a homogenous 3D position. */
  POSITION4F(4),
  /** Represents a 3D normal vector. */
  NORMAL(3),
  /** Represents a RGB color. */
  COLOR_RGB(3),
  /** Represents a RGBA color. */
  COLOR_RGBA(4),
  /** Represents a texture coordinate. */
  TEXTURE_UV(2);

  private final int size;

  VertexAttributes(int size) {
    this.size = size;
  }

  /** {@inheritDoc} */
  @Override
  public int getSize() {
    return this.size;
  }
}
