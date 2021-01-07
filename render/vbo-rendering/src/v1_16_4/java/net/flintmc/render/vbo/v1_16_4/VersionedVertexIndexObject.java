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

package net.flintmc.render.vbo.v1_16_4;

import static org.lwjgl.opengl.GL33.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL33.GL_ELEMENT_ARRAY_BUFFER_BINDING;
import static org.lwjgl.opengl.GL33.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL33.glBindBuffer;
import static org.lwjgl.opengl.GL33.glBufferData;
import static org.lwjgl.opengl.GL33.glDeleteBuffers;
import static org.lwjgl.opengl.GL33.glGenBuffers;
import static org.lwjgl.opengl.GL33.glGetInteger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.vbo.VboDrawMode;
import net.flintmc.render.vbo.VertexIndexObject;

/**
 * {@inheritDoc}
 */
@Implement(value = VertexIndexObject.class, version = "1.16.4")
public class VersionedVertexIndexObject implements VertexIndexObject {

  private final List<Integer> indices;
  private final int id;
  private final VboDrawMode drawMode;

  private boolean isAvailable;
  private boolean deleted;
  private int oldEbo;

  @AssistedInject
  private VersionedVertexIndexObject() {
    this(VboDrawMode.TRIANGLES);
  }

  @AssistedInject
  private VersionedVertexIndexObject(@Assisted VboDrawMode drawMode) {
    this.indices = new ArrayList<>();
    this.id = glGenBuffers();
    this.isAvailable = false;
    this.deleted = false;
    this.oldEbo = 0;
    this.drawMode = drawMode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addIndices(int... indices) {
    if (this.isAvailable) {
      throw new IllegalStateException(
          "This EBO has already been pushed to the GPU, indices can't be added anymore.");
    }
    for (int index : indices) {
      this.indices.add(index);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Integer> getIndices() {
    return Collections.unmodifiableList(this.indices);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getSize() {
    return this.indices.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void pushToGPU() {
    if (this.isAvailable) {
      throw new IllegalStateException("This EBO has already been pushed to the GPU.");
    }

    int[] indicesArray = new int[indices.size()];
    for (int i = 0; i < this.indices.size(); i++) {
      indicesArray[i] = this.indices.get(i);
    }

    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesArray, GL_STATIC_DRAW);

    this.isAvailable = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getID() {
    return this.id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bind() {
    this.oldEbo = glGetInteger(GL_ELEMENT_ARRAY_BUFFER_BINDING);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unbind() {
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, Math.max(this.oldEbo, 0));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAvailable() {
    return this.isAvailable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VboDrawMode getDrawMode() {
    return this.drawMode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete() {
    if (this.deleted) {
      throw new IllegalStateException("The EBO was already deleted.");
    }
    this.bind();
    glDeleteBuffers(this.id);
    this.unbind();
    this.deleted = true;
  }
}
