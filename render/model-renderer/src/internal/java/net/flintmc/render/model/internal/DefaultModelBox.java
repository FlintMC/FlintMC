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

package net.flintmc.render.model.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.model.ModelBox;

@Implement(ModelBox.class)
public class DefaultModelBox implements ModelBox {

  private float positionX1;
  private float positionX2;
  private float positionY1;
  private float positionY2;
  private float positionZ1;
  private float positionZ2;
  private List<TexturedQuad> texturedQuads = new ArrayList<>();

  @AssistedInject
  private DefaultModelBox() {
  }

  @AssistedInject
  private DefaultModelBox(
      @Assisted("textureOffsetX") float textureOffsetX,
      @Assisted("textureOffsetY") float textureOffsetY,
      @Assisted("positionX") float positionX,
      @Assisted("positionY") float positionY,
      @Assisted("positionZ") float positionZ,
      @Assisted("width") float width,
      @Assisted("height") float height,
      @Assisted("depth") float depth,
      @Assisted("deltaX") float deltaX,
      @Assisted("deltaY") float deltaY,
      @Assisted("deltaZ") float deltaZ,
      @Assisted("mirror") boolean mirror,
      @Assisted("textureWidth") float textureWidth,
      @Assisted("textureHeight") float textureHeight,
      TexturedQuad.Factory texturedQuadFactory,
      TexturedQuad.VertexPosition.Factory vertexPositionFactory) {

    this.positionX1 = positionX;
    this.positionY1 = positionY;
    this.positionZ1 = positionZ;
    this.positionX2 = positionX + width;
    this.positionY2 = positionY + height;
    this.positionZ2 = positionZ + depth;
    float f = positionX + width;
    float f1 = positionY + height;
    float f2 = positionZ + depth;
    positionX = positionX - deltaX;
    positionY = positionY - deltaY;
    positionZ = positionZ - deltaZ;
    f = f + deltaX;
    f1 = f1 + deltaY;
    f2 = f2 + deltaZ;
    if (mirror) {
      float f3 = f;
      f = positionX;
      positionX = f3;
    }

    TexturedQuad.VertexPosition vertex0 =
        vertexPositionFactory.create(0.0F, 8.0F, f, positionY, positionZ);
    TexturedQuad.VertexPosition vertex1 =
        vertexPositionFactory.create(8.0F, 8.0F, f, f1, positionZ);
    TexturedQuad.VertexPosition vertex2 =
        vertexPositionFactory.create(8.0F, 0.0F, positionX, f1, positionZ);
    TexturedQuad.VertexPosition vertex3 =
        vertexPositionFactory.create(0.0F, 0.0F, positionX, positionY, f2);
    TexturedQuad.VertexPosition vertex4 =
        vertexPositionFactory.create(0.0F, 8.0F, f, positionY, f2);
    TexturedQuad.VertexPosition vertex5 = vertexPositionFactory.create(8.0F, 8.0F, f, f1, f2);
    TexturedQuad.VertexPosition vertex6 =
        vertexPositionFactory.create(8.0F, 0.0F, positionX, f1, f2);
    TexturedQuad.VertexPosition vertex7 =
        vertexPositionFactory.create(0.0F, 0.0F, positionX, positionY, positionZ);

    float f4 = textureOffsetX;
    float f5 = textureOffsetX + depth;
    float f6 = textureOffsetX + depth + width;
    float f7 = textureOffsetX + depth + width + width;
    float f8 = textureOffsetX + depth + width + depth;
    float f9 = textureOffsetX + depth + width + depth + width;
    float f10 = textureOffsetY;
    float f11 = textureOffsetY + depth;
    float f12 = textureOffsetY + depth + height;

    TexturedQuad.VertexPosition[] quad2Vertices = {vertex4, vertex3, vertex7, vertex0},
        quad3Vertices = {vertex1, vertex2, vertex6, vertex5},
        quad1Vertices = {vertex7, vertex3, vertex6, vertex2},
        quad4Vertices = {vertex0, vertex7, vertex2, vertex1},
        quad0Vertices = {vertex4, vertex0, vertex1, vertex5},
        quad5Vertices = {vertex3, vertex4, vertex5, vertex6};

    handleTexturedVertices(quad2Vertices, f5, f10, f6, f11, textureWidth, textureHeight, mirror);
    handleTexturedVertices(quad3Vertices, f6, f11, f7, f10, textureWidth, textureHeight, mirror);
    handleTexturedVertices(quad1Vertices, f4, f11, f5, f12, textureWidth, textureHeight, mirror);
    handleTexturedVertices(quad4Vertices, f5, f11, f6, f12, textureWidth, textureHeight, mirror);
    handleTexturedVertices(quad0Vertices, f6, f11, f8, f12, textureWidth, textureHeight, mirror);
    handleTexturedVertices(quad5Vertices, f8, f11, f9, f12, textureWidth, textureHeight, mirror);

    this.texturedQuads.set(2, texturedQuadFactory.create(0, -1, 0, quad2Vertices));
    this.texturedQuads.set(3, texturedQuadFactory.create(0, 1, 0, quad3Vertices));
    this.texturedQuads.set(1, texturedQuadFactory.create(mirror ? 1 : -1, 0, 0, quad1Vertices));
    this.texturedQuads.set(4, texturedQuadFactory.create(0, 0, -1, quad4Vertices));
    this.texturedQuads.set(0, texturedQuadFactory.create(mirror ? -1 : 1, 0, 0, quad0Vertices));
    this.texturedQuads.set(5, texturedQuadFactory.create(0, 0, 1, quad5Vertices));
  }

  private void handleTexturedVertices(
      TexturedQuad.VertexPosition[] vertexPositions,
      float u1,
      float v1,
      float u2,
      float v2,
      float texWidth,
      float texHeight,
      boolean mirrorIn) {
    float f = 0.0F / texWidth;
    float f1 = 0.0F / texHeight;
    vertexPositions[0] = vertexPositions[0].setTextureUV(u2 / texWidth - f, v1 / texHeight + f1);
    vertexPositions[1] = vertexPositions[1].setTextureUV(u1 / texWidth + f, v1 / texHeight + f1);
    vertexPositions[2] = vertexPositions[2].setTextureUV(u1 / texWidth + f, v2 / texHeight - f1);
    vertexPositions[3] = vertexPositions[3].setTextureUV(u2 / texWidth - f, v2 / texHeight - f1);
    if (mirrorIn) {
      int i = vertexPositions.length;

      for (int j = 0; j < i / 2; ++j) {
        TexturedQuad.VertexPosition vertex = vertexPositions[j];
        vertexPositions[j] = vertexPositions[i - 1 - j];
        vertexPositions[i - 1 - j] = vertex;
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPositionX1() {
    return positionX1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultModelBox setPositionX1(float positionX1) {
    this.positionX1 = positionX1;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPositionX2() {
    return positionX2;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultModelBox setPositionX2(float positionX2) {
    this.positionX2 = positionX2;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPositionY1() {
    return positionY1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultModelBox setPositionY1(float positionY1) {
    this.positionY1 = positionY1;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPositionY2() {
    return positionY2;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultModelBox setPositionY2(float positionY2) {
    this.positionY2 = positionY2;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPositionZ1() {
    return positionZ1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultModelBox setPositionZ1(float positionZ1) {
    this.positionZ1 = positionZ1;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPositionZ2() {
    return positionZ2;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultModelBox setPositionZ2(float positionZ2) {
    this.positionZ2 = positionZ2;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelBox setTexturedQuads(List<TexturedQuad> texturedQuads) {
    this.texturedQuads = texturedQuads;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<TexturedQuad> getTexturedQuads() {
    return this.texturedQuads;
  }

  @Implement(TexturedQuad.class)
  public static class DefaultTexturedQuad implements TexturedQuad {

    private final float normalX;
    private final float normalY;
    private final float normalZ;
    private final VertexPosition[] vertexPositions;

    @AssistedInject
    private DefaultTexturedQuad(
        @Assisted("normalX") float normalX,
        @Assisted("normalY") float normalY,
        @Assisted("normalZ") float normalZ,
        @Assisted VertexPosition[] vertexPositions) {
      this.normalX = normalX;
      this.normalY = normalY;
      this.normalZ = normalZ;
      this.vertexPositions = vertexPositions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getNormalX() {
      return this.normalX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getNormalY() {
      return this.normalY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getNormalZ() {
      return this.normalZ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VertexPosition[] getVertexPositions() {
      return this.vertexPositions;
    }

    @Implement(VertexPosition.class)
    public static class DefaultVertexPosition implements VertexPosition {

      private final float positionX;
      private final float positionY;
      private final float positionZ;
      private float textureU;
      private float textureV;

      @AssistedInject
      private DefaultVertexPosition(
          @Assisted("textureU") float textureU,
          @Assisted("textureV") float textureV,
          @Assisted("positionX") float positionX,
          @Assisted("positionY") float positionY,
          @Assisted("positionZ") float positionZ) {
        this.textureU = textureU;
        this.textureV = textureV;
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public float getTextureU() {
        return textureU;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public float getTextureV() {
        return textureV;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public float getPositionX() {
        return positionX;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public float getPositionY() {
        return positionY;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public float getPositionZ() {
        return positionZ;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public VertexPosition setTextureUV(float u, float v) {
        this.textureU = u;
        this.textureV = v;
        return this;
      }
    }
  }
}
