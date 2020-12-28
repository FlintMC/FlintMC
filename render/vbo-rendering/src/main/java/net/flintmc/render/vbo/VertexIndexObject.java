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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.util.List;

/**
 * Represents an OpenGL buffer object containing indices of vertices contained in a vertex buffer
 * object.
 */
public interface VertexIndexObject {

  /**
   * Adds indices to the buffer.
   *
   * @param indices the indices to be added.
   */
  void addIndices(int... indices);

  /**
   * @return a list of indices currently contained in this buffer. The list should not be modified.
   */
  List<Integer> getIndices();

  /** @return the number of indices currently added. */
  int getSize();

  /** Pushes the current list of indices to the GPU. Should usually not be called manually. */
  void pushToGPU();

  /** @return the OpenGL name of the buffer object. */
  int getID();

  /** Binds this buffer object with OpenGL. */
  void bind();

  /** Unbinds this buffer object and binds the buffer object that was previously bound (if any). */
  void unbind();

  /** @return true, if this buffer object was pushed with OpenGL. */
  boolean isAvailable();

  /** @return the {@link VboDrawMode} this index buffer expects to be drawn with. */
  VboDrawMode getDrawMode();

  /** Deletes this EBO with OpenGL. */
  void delete();

  @AssistedFactory(VertexIndexObject.class)
  interface Factory {

    /**
     * Creates a new {@link VertexIndexObject} with TRIANGLES as draw mode.
     *
     * @return the new {@link VertexIndexObject}.
     */
    VertexIndexObject create();

    /**
     * Creates a new {@link VertexIndexObject}.
     *
     * @param drawMode the draw mode this index buffer expects to be drawn with.
     * @return the new {@link VertexIndexObject}.
     */
    VertexIndexObject create(@Assisted VboDrawMode drawMode);
  }
}
