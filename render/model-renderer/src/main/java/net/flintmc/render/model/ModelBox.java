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

package net.flintmc.render.model;

import java.util.Collection;
import java.util.List;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

public interface ModelBox {

  float getPositionX1();

  ModelBox setPositionX1(float x1);

  float getPositionX2();

  ModelBox setPositionX2(float x2);

  float getPositionY1();

  ModelBox setPositionY1(float y1);

  float getPositionY2();

  ModelBox setPositionY2(float y2);

  float getPositionZ1();

  ModelBox setPositionZ1(float z1);

  float getPositionZ2();

  ModelBox setPositionZ2(float z2);

  Collection<TexturedQuad> getTexturedQuads();

  ModelBox setTexturedQuads(List<TexturedQuad> texturedQuads);

  interface TexturedQuad {

    float getNormalX();

    float getNormalY();

    float getNormalZ();

    VertexPosition[] getVertexPositions();

    @AssistedFactory(TexturedQuad.class)
    interface Factory {

      TexturedQuad create(
          @Assisted("normalX") float normalX,
          @Assisted("normalY") float normalY,
          @Assisted("normalZ") float normalZ,
          @Assisted VertexPosition[] vertexPositions);
    }

    interface VertexPosition {

      float getTextureU();

      float getTextureV();

      float getPositionX();

      float getPositionY();

      float getPositionZ();

      VertexPosition setTextureUV(float u, float v);

      @AssistedFactory(VertexPosition.class)
      interface Factory {

        VertexPosition create(
            @Assisted("textureU") float textureU,
            @Assisted("textureV") float textureV,
            @Assisted("positionX") float positionX,
            @Assisted("positionY") float positionY,
            @Assisted("positionZ") float positionZ);
      }
    }
  }

  @AssistedFactory(ModelBox.class)
  interface Factory {

    ModelBox create();

    ModelBox create(
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
        @Assisted("textureHeight") float textureHeight);
  }
}
