package net.flintmc.render.model;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Collection;
import java.util.List;

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
