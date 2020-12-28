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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.vbo.VertexBufferObject;
import net.flintmc.render.vbo.VertexBuilder;
import net.flintmc.render.vbo.VertexFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

/** {@inheritDoc} */
@Implement(value = VertexBufferObject.class, version = "1.15.2")
public class VersionedVertexBufferObject implements VertexBufferObject {

  private final VertexFormat vertexFormat;
  private final VertexBuilder.Factory vertexBuilderFactory;
  private final int id;

  private List<VertexBuilder> vertices;
  private boolean isAvailable;
  private boolean deleted;
  private int previousVbo;

  @AssistedInject
  private VersionedVertexBufferObject(
      @Assisted VertexFormat vertexFormat, VertexBuilder.Factory vertexBuilderFactory) {
    this.vertexFormat = vertexFormat;
    this.vertexBuilderFactory = vertexBuilderFactory;
    this.vertices = new ArrayList<>();
    this.id = glGenBuffers();
    this.isAvailable = false;
    this.deleted = false;
  }

  /** {@inheritDoc} */
  @Override
  public VertexBuilder addVertex() {
    if (isAvailable)
      throw new IllegalStateException(
          "This VBO is already pushed to the GPU, vertices can't be added anymore.");
    VertexBuilder builder = this.vertexBuilderFactory.create(this);
    this.vertices.add(builder);
    return builder;
  }

  /** {@inheritDoc} */
  @Override
  public void addVertex(VertexBuilder vertexBuilder) {
    if (isAvailable)
      throw new IllegalStateException(
          "This VBO is already pushed to the GPU, vertices can't be added anymore.");
    this.vertices.add(vertexBuilder);
  }

  /** {@inheritDoc} */
  @Override
  public List<VertexBuilder> getVertices() {
    return Collections.unmodifiableList(this.vertices);
  }

  public int getVertexCount() {
    return this.vertices.size();
  }

  /** {@inheritDoc} */
  @Override
  public void pushToGPU() {
    if (isAvailable) throw new IllegalStateException("This VBO is already pushed to the GPU.");
    int totalSize = vertices.size() * vertexFormat.getVertexSize();
    float[] buffer = new float[totalSize];
    int offset = 0;
    for (VertexBuilder vertex : this.vertices) {
      offset += vertex.write(buffer, offset);
    }

    glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

    this.isAvailable = true;
  }

  /** {@inheritDoc} */
  @Override
  public int getID() {
    return this.id;
  }

  /** {@inheritDoc} */
  @Override
  public void bind() {
    this.previousVbo = glGetInteger(GL_ARRAY_BUFFER_BINDING);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ARRAY_BUFFER, this.id);
  }

  /** {@inheritDoc} */
  @Override
  public void unbind() {
    glBindBuffer(GL_ARRAY_BUFFER, Math.max(this.previousVbo, 0));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAvailable() {
    return this.isAvailable;
  }

  /** {@inheritDoc} */
  @Override
  public VertexFormat getFormat() {
    return this.vertexFormat;
  }

  /** {@inheritDoc} */
  @Override
  public void delete() {
    if (this.deleted) throw new IllegalStateException("The VBO was already deleted.");
    this.bind();
    glDeleteBuffers(this.id);
    this.unbind();
    this.deleted = true;
  }
}
