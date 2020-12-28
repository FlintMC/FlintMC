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

/** Represents a vertex attribute format. */
public interface VertexFormat {

  /** @return the number of generic vertex attribute per vertex this vertex format defines. */
  int getAttributeCount();

  /**
   * @return the size of a vertex in floats (same as the sum over the sizes of all generic vertex
   *     attributes contained in this format).
   */
  int getVertexSize();

  /**
   * @return a list of all generic {@link VertexAttribute}s currently contained in this format. The
   *     list shall not be modified.
   */
  List<VertexAttribute> getAttributes();

  /**
   * Creates a new OpenGL vertex array object based on this format.
   *
   * @return the OpenGL name of the newly creates vertex array object.
   */
  int createVAO();

  /**
   * Uploads this format to a given OpenGL vertex array object.
   *
   * @param vao the OpenGL name of the vertex array object this format should be uploaded to.
   */
  void pushToGPU(int vao);

  @AssistedFactory(VertexFormat.class)
  interface Factory {

    /**
     * Creates a new {@link VertexFormat}.
     *
     * @param attributes the generic vertex attributes that will make up a vertex according to the
     *     format.
     * @return the new {@link VertexFormat}.
     */
    VertexFormat create(@Assisted List<VertexAttribute> attributes);
  }
}
