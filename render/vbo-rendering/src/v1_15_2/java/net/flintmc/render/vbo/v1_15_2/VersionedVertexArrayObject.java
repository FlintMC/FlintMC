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

import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL33.GL_QUADS;
import static org.lwjgl.opengl.GL33.GL_TRIANGLES;
import static org.lwjgl.opengl.GL33.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL33.GL_VERTEX_ARRAY_BINDING;
import static org.lwjgl.opengl.GL33.glBindVertexArray;
import static org.lwjgl.opengl.GL33.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL33.glDrawElements;
import static org.lwjgl.opengl.GL33.glGetInteger;

import java.nio.IntBuffer;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.vbo.VboDrawMode;
import net.flintmc.render.vbo.VertexArrayObject;
import net.flintmc.render.vbo.VertexBufferObject;
import net.flintmc.render.vbo.VertexFormat;
import net.flintmc.render.vbo.VertexIndexObject;

/**
 * {@inheritDoc}
 */
@Implement(VertexArrayObject.class)
public class VersionedVertexArrayObject implements VertexArrayObject {

  private final VertexFormat format;
  private final VertexBufferObject vbo;

  private final int id;
  private int oldId;
  private boolean deleted;

  @AssistedInject
  private VersionedVertexArrayObject(@Assisted VertexBufferObject vbo) {
    this(vbo, () -> {
    });
  }

  @AssistedInject
  private VersionedVertexArrayObject(
      @Assisted VertexBufferObject vbo, @Assisted Runnable bindCallback) {
    this.format = vbo.getFormat();
    this.vbo = vbo;

    this.id = this.format.createVAO();
    this.deleted = false;

    this.bind();

    this.vbo.bind();
    this.vbo.pushToGPU();

    this.format.pushToGPU(this.id);

    bindCallback.run();

    this.vbo.unbind();
    this.unbind();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void draw(VertexIndexObject ebo) {
    this.bind();
    this.vbo.bind();
    ebo.bind();
    this.drawWithoutBind(ebo);
    this.unbind();
    this.vbo.unbind();
    ebo.unbind();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void draw(IntBuffer indices, VboDrawMode drawMode) {
    this.bind();
    this.vbo.bind();
    if (drawMode == VboDrawMode.TRIANGLES) {
      glDrawElements(GL_TRIANGLES, indices);
    } else if (drawMode == VboDrawMode.QUADS) {
      glDrawElements(GL_QUADS, indices);
    } else if (drawMode == VboDrawMode.POLYGON) {
      glDrawElements(GL_POLYGON, indices);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void drawWithoutBind(VertexIndexObject ebo) {
    if (this.deleted) {
      throw new IllegalStateException(
          "The VAO has already been deleted and can not be used for drawing anymore.");
    }
    if (!ebo.isAvailable()) {
      ebo.pushToGPU();
    }
    if (ebo.getDrawMode() == VboDrawMode.TRIANGLES) {
      glDrawElements(GL_TRIANGLES, ebo.getSize(), GL_UNSIGNED_INT, 0);
    } else if (ebo.getDrawMode() == VboDrawMode.QUADS) {
      glDrawElements(GL_QUADS, ebo.getSize(), GL_UNSIGNED_INT, 0);
    } else if (ebo.getDrawMode() == VboDrawMode.POLYGON) {
      glDrawElements(GL_POLYGON, ebo.getSize(), GL_UNSIGNED_INT, 0);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexFormat getFormat() {
    return this.format;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexBufferObject getVBO() {
    return this.vbo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bind() {
    this.oldId = glGetInteger(GL_VERTEX_ARRAY_BINDING);
    glBindVertexArray(this.id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unbind() {
    glBindVertexArray(this.oldId);
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
  public void delete() {
    if (this.deleted) {
      throw new IllegalStateException("The VAO was already deleted.");
    }
    this.vbo.delete();
    this.bind();
    glDeleteVertexArrays(this.id);
    this.unbind();
    this.deleted = true;
  }
}
