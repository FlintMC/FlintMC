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

/** Represents an OpenGL vertex buffer object (VBO). */
public interface VertexBufferObject {

  /**
   * Creates a new {@link VertexBuilder} and adds it to the list of vertices.
   *
   * @return a new {@link VertexBuilder} that should be used to construct the vertex.
   */
  VertexBuilder addVertex();

  /**
   * Adds a {@link VertexBuilder} to the list of vertices.
   *
   * @param vertexBuilder the {@link VertexBuilder} to be added.
   */
  void addVertex(VertexBuilder vertexBuilder);

  /** @return a list of currently added vertices. The list shall not be modified. */
  List<VertexBuilder> getVertices();

  /** @return the number of vertices contained in this vertex buffer object. */
  int getVertexCount();

  /**
   * Pushes the vertices to the OpenGL vertex buffer object. Should usually not be called manually.
   */
  void pushToGPU();

  /** @return the OpenGL name of this vertex buffer object. */
  int getID();

  /** Binds this vertex buffer object with OpenGL. */
  void bind();

  /**
   * Unbinds this vertex buffer object and rebinds the vertex buffer object that was previously
   * bound (if any).
   */
  void unbind();

  /**
   * @return true, if the vertices of this vertex buffer object have been pushed via OpenGL and the
   *     VBO is therefore ready to be used in a draw call.
   */
  boolean isAvailable();

  /**
   * @return the {@link VertexFormat} the vertices of this vertex buffer object should comply with.
   */
  VertexFormat getFormat();

  /** Deletes this vertex buffer object. */
  void delete();

  @AssistedFactory(VertexBufferObject.class)
  interface Factory {

    /**
     * Creates a new {@link VertexBufferObject}.
     *
     * @param vertexFormat the {@link VertexFormat} the vertices should be in.
     * @return the new {@link VertexBufferObject}.
     */
    VertexBufferObject create(@Assisted VertexFormat vertexFormat);
  }
}
