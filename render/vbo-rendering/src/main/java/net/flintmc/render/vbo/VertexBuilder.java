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

/** Represents a vertex to be added to a vertex buffer object. */
public interface VertexBuilder {

  /**
   * Sets the next 3D position attribute of the vertex.
   *
   * @param x the x component of the position.
   * @param y the y component of the position.
   * @param z the z component of the position.
   * @return the instance.
   */
  VertexBuilder position(float x, float y, float z);

  /**
   * Sets the next homogenous 3D position attribute of the vertex.
   *
   * @param x the x component of the position.
   * @param y the y component of the position.
   * @param z the z component of the position.
   * @param w the w component of the position.
   * @return the instance.
   */
  VertexBuilder position(float x, float y, float z, float w);

  /**
   * Sets the next 3D normal attribute of the vertex.
   *
   * @param x the x component of the normal.
   * @param y the y component of the normal.
   * @param z the z component of the normal.
   * @return the instance.
   */
  VertexBuilder normal(float x, float y, float z);

  /**
   * Sets the next RGB color attribute of the vertex. Values should be between 0 and 1.
   *
   * @param r the red value of the color.
   * @param g the green value of the color.
   * @param b the blue value of the color.
   * @return the instance.
   */
  VertexBuilder color(float r, float g, float b);

  /**
   * Sets the next RGB color attribute of the vertex. Values should be between 0 and 255.
   *
   * @param r the red value of the color.
   * @param g the green value of the color.
   * @param b the blue value of the color.
   * @return the instance.
   */
  VertexBuilder color(byte r, byte g, byte b);

  /**
   * Sets the next RGBA color attribute of the vertex. Values should be between 0 and 255.
   *
   * @param r the red value of the color.
   * @param g the green value of the color.
   * @param b the blue value of the color.
   * @param alpha the alpha value of the color.
   * @return the instance.
   */
  VertexBuilder color(byte r, byte g, byte b, byte alpha);

  /**
   * Sets the next RGBA color attribute of the vertex.
   *
   * @param rgba the color values encoded in a float. The bytes are interpreted left-to-right from R
   *     to alpha.
   * @return the instance.
   */
  VertexBuilder color(float rgba);

  /**
   * Sets the next RGBA color attribute of the vertex. Values should be between 0 and 1.
   *
   * @param r the red value of the color.
   * @param g the green value of the color.
   * @param b the blue value of the color.
   * @param alpha the alpha value of the color.
   * @return the instance.
   */
  VertexBuilder color(float r, float g, float b, float alpha);

  /**
   * Sets the next texture coordinate attribute of the vertex.
   *
   * @param u the u component.
   * @param v the v component.
   * @return the instance.
   */
  VertexBuilder texture(float u, float v);

  /**
   * Sets the next custom attribute of the vertex.
   *
   * @param values the value for the attribute.
   * @return the instance.
   */
  VertexBuilder custom(float... values);

  /** @return a new {@link VertexBuilder} to add a vertex to the same vertex buffer object. */
  VertexBuilder next();

  AttributeValueHandler getPos3fHandler();

  AttributeValueHandler getPos4fHandler();

  AttributeValueHandler getNormalHandler();

  AttributeValueHandler getRgbHandler();

  AttributeValueHandler getRgbaHandler();

  AttributeValueHandler getTextureHandler();

  AttributeValueHandler getCustomHandler();

  interface AttributeValueHandler{

    void addFloats(float... floats);

    void writeFloats(float[] buffer, int offset);

    void clear();
  }

  /**
   * Writes this vertex to a buffer.
   *
   * @param buffer the buffer to write this vertex to.
   * @param offset the offset at which to start writing the vertex.
   * @return the number of floats that have been written.
   */
  int write(float[] buffer, int offset);

  @AssistedFactory(VertexBuilder.class)
  interface Factory {

    /**
     * Creates a new {@link VertexBuilder}.
     *
     * @param vbo the vertex buffer object the vertex refers to.
     * @return the new {@link VertexBuilder}.
     */
    VertexBuilder create(@Assisted VertexBufferObject vbo);
  }
}
