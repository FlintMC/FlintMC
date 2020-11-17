package net.flintmc.render.model.internal;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.model.ModelBox;

import java.util.Collection;
import java.util.HashSet;

@Implement(ModelBox.class)
public class DefaultModelBox implements ModelBox {

  private float positionX1;
  private float positionX2;
  private float positionY1;
  private float positionY2;
  private float positionZ1;
  private float positionZ2;
  private Collection<TexturedQuad> texturedQuads = new HashSet<>();

  @AssistedInject
  private DefaultModelBox() {
  }

  public float getPositionX1() {
    return positionX1;
  }

  public DefaultModelBox setPositionX1(float positionX1) {
    this.positionX1 = positionX1;
    return this;
  }

  public float getPositionX2() {
    return positionX2;
  }

  public DefaultModelBox setPositionX2(float positionX2) {
    this.positionX2 = positionX2;
    return this;
  }

  public float getPositionY1() {
    return positionY1;
  }

  public DefaultModelBox setPositionY1(float positionY1) {
    this.positionY1 = positionY1;
    return this;
  }

  public float getPositionY2() {
    return positionY2;
  }

  public DefaultModelBox setPositionY2(float positionY2) {
    this.positionY2 = positionY2;
    return this;
  }

  public float getPositionZ1() {
    return positionZ1;
  }

  public DefaultModelBox setPositionZ1(float positionZ1) {
    this.positionZ1 = positionZ1;
    return this;
  }

  public float getPositionZ2() {
    return positionZ2;
  }

  public DefaultModelBox setPositionZ2(float positionZ2) {
    this.positionZ2 = positionZ2;
    return this;
  }

  public ModelBox setTexturedQuads(Collection<TexturedQuad> texturedQuads) {
    this.texturedQuads = texturedQuads;
    return this;
  }

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

    public float getNormalX() {
      return this.normalX;
    }

    public float getNormalY() {
      return this.normalY;
    }

    public float getNormalZ() {
      return this.normalZ;
    }

    public VertexPosition[] getVertexPositions() {

      return this.vertexPositions;
    }

    @Implement(VertexPosition.class)
    public static class DefaultVertexPosition implements VertexPosition {
      private final float textureU;
      private final float textureV;
      private final float positionX;
      private final float positionY;
      private final float positionZ;

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

      public float getTextureU() {
        return textureU;
      }

      public float getTextureV() {
        return textureV;
      }

      public float getPositionX() {
        return positionX;
      }

      public float getPositionY() {
        return positionY;
      }

      public float getPositionZ() {
        return positionZ;
      }
    }
  }
}
