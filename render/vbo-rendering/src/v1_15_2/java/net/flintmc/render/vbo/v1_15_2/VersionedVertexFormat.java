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

package net.flintmc.render.vbo.v1_15_2;

import static org.lwjgl.opengl.GL33.GL_FLOAT;
import static org.lwjgl.opengl.GL33.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL33.glGenVertexArrays;
import static org.lwjgl.opengl.GL33.glVertexAttribPointer;

import java.util.Collections;
import java.util.List;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.vbo.VertexAttribute;
import net.flintmc.render.vbo.VertexFormat;

/**
 * {@inheritDoc}
 */
@Implement(value = VertexFormat.class)
public class VersionedVertexFormat implements VertexFormat {

  private final List<VertexAttribute> attributes;
  private final int stride;

  @AssistedInject
  private VersionedVertexFormat(@Assisted List<VertexAttribute> attributes) {
    this.attributes = attributes;
    this.stride =
        this.attributes.stream().mapToInt(VertexAttribute::getSize).reduce(0, Integer::sum);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getAttributeCount() {
    return attributes.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getVertexSize() {
    return this.stride;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<VertexAttribute> getAttributes() {
    return Collections.unmodifiableList(this.attributes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int createVAO() {
    return glGenVertexArrays();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void pushToGPU(int vao) {
    int index = 0;
    int offset = 0;

    for (VertexAttribute attribute : this.attributes) {
      glVertexAttribPointer(
          index, attribute.getSize(), GL_FLOAT, false, this.getVertexSize() * Float.BYTES, offset);
      glEnableVertexAttribArray(index);
      index++;
      offset += attribute.getSize() * Float.BYTES;
    }
  }
}
