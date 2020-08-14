package net.labyfy.internal.component.render;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.MatrixStack;
import net.labyfy.component.render.VertexBox;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexQuad;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class VertexBoxImpl implements VertexBox {

  private final VertexQuad back;
  private final VertexQuad front;
  private final VertexQuad right;
  private final VertexQuad left;
  private final VertexQuad top;
  private final VertexQuad bottom;

  private Supplier<Vector3f> position;
  private Supplier<Vector3f> dimensions;
  private Supplier<Color> color;
  private Supplier<Vector2f> textureDensity;
  private Supplier<Vector2f> textureOffset;
  private IntSupplier lightMap;

  private VertexBoxImpl(
      Supplier<Vector3f> position,
      Supplier<Vector3f> dimensions,
      VertexQuad.Builder.Factory vertexQuadBuilderFactory,
      Supplier<Color> color,
      Supplier<Vector2f> textureDensity, Supplier<Vector2f> textureOffset, IntSupplier lightMap
  ) {

    this.position = position;
    this.dimensions = dimensions;
    this.color = color;
    this.textureDensity = textureDensity;
    this.textureOffset = textureOffset;
    this.lightMap = lightMap;

    this.back = vertexQuadBuilderFactory.create()
        .withVertices(
            this::getPosition,
            () -> this.getPosition().add(this.getWidth(), 0, 0),
            () -> this.getPosition().add(this.getWidth(), this.getHeight(), 0),
            () -> this.getPosition().add(0, this.getHeight(), 0)
        )
        .withTextureUVs(
            () -> new Vector2f(getTextureOffsetX() + ((getDepth() + getWidth() + getDepth() + getWidth()) / getTextureDensityX()), getTextureOffsetY() + ((getDepth() + getHeight()) / getTextureDensityY())),
            () -> new Vector2f(getTextureOffsetX() + ((getDepth() + getWidth() + getDepth()) / getTextureDensityX()), getTextureOffsetY() + ((getDepth() + getHeight()) / getTextureDensityY())),
            () -> new Vector2f(getTextureOffsetX() + ((getDepth() + getWidth() + getDepth()) / getTextureDensityX()), getTextureOffsetY() + (getDepth() / getTextureDensityY())),
            () -> new Vector2f(getTextureOffsetX() + ((getDepth() + getWidth() + getDepth() + getWidth()) / getTextureDensityX()), getTextureOffsetY() + (getDepth() / getTextureDensityY()))
        )
        .withColor(this.color)
        .withLightMap(() -> this.lightMap.getAsInt())
        .build();

    this.front = vertexQuadBuilderFactory.create()
        .withVertices(
            () -> this.getPosition().add(0, this.getHeight(), this.getDepth()),
            () -> this.getPosition().add(0, 0, this.getDepth()),
            () -> this.getPosition().add(this.getWidth(), 0, this.getDepth()),
            () -> this.getPosition().add(this.getWidth(), this.getHeight(), this.getDepth())
        )
        .withTextureUVs(
            () -> new Vector2f(getTextureOffsetX() + getDepth() / getTextureDensityX(), getTextureOffsetY() + getDepth() / getTextureDensityY()),
            () -> new Vector2f(getTextureOffsetX() + getDepth() / getTextureDensityX(), getTextureOffsetY() + (getDepth() + getHeight()) / getTextureDensityY()),
            () -> new Vector2f(getTextureOffsetX() + (getDepth() + getWidth()) / getTextureDensityX(), getTextureOffsetY() + (getDepth() + getHeight()) / getTextureDensityY()),
            () -> new Vector2f(getTextureOffsetX() + (getDepth() + getWidth()) / getTextureDensityX(), getTextureOffsetY() + getDepth() / getTextureDensityY())
        )
        .withColor(() -> this.color.get())
        .withLightMap(() -> this.lightMap.getAsInt())
        .build();

    this.right = vertexQuadBuilderFactory.create()
        .withVertices(
            this::getPosition,
            () -> this.getPosition().add(0, 0, this.getDepth()),
            () -> this.getPosition().add(0, this.getHeight(), this.getDepth()),
            () -> this.getPosition().add(0, this.getHeight(), 0)
        )
        .withTextureUVs(
            () -> new Vector2f(getTextureOffsetX() + (getDepth() + getWidth() + getDepth()) / getTextureDensityX(), getTextureOffsetY() + (getDepth() + getHeight()) / getTextureDensityY()),
            () -> new Vector2f(getTextureOffsetX() + (getDepth() + getWidth()) / getTextureDensityX(), getTextureOffsetY() + (getDepth() + getHeight()) / getTextureDensityY()),
            () -> new Vector2f(getTextureOffsetX() + (getDepth() + getWidth()) / getTextureDensityX(), getTextureOffsetY() + getDepth() / getTextureDensityY()),
            () -> new Vector2f(getTextureOffsetX() + (getDepth() + getWidth() + getDepth()) / getTextureDensityX(), getTextureOffsetY() + getDepth() / getTextureDensityY())
        )
        .withColor(() -> this.color.get())
        .withLightMap(() -> this.lightMap.getAsInt())
        .build();

    this.left = vertexQuadBuilderFactory.create()
        .withVertices(
            () -> this.getPosition().add(this.getWidth(), 0, 0),
            () -> this.getPosition().add(this.getWidth(), 0, this.getDepth()),
            () -> this.getPosition().add(this.getDimensions()),
            () -> this.getPosition().add(this.getWidth(), this.getHeight(), 0)
        )
        .withTextureUVs(
            () -> new Vector2f(getTextureOffsetX(), getTextureOffsetY() + (getDepth() + getHeight()) / getTextureDensityY()),
            () -> new Vector2f(getTextureOffsetX() + getDepth() / getTextureDensityX(), getTextureOffsetY() + (getDepth() + getHeight()) / getTextureDensityY()),
            () -> new Vector2f(getTextureOffsetX() + getDepth() / getTextureDensityX(), getTextureOffsetY() + getDepth() / getTextureDensityY()),
            () -> new Vector2f(getTextureOffsetX(), getTextureOffsetY() + getDepth() / getTextureDensityY())
        )
        .withColor(() -> this.color.get())
        .withLightMap(() -> this.lightMap.getAsInt())
        .build();

    this.top = vertexQuadBuilderFactory.create()
        .withVertices(
            () -> this.getPosition().add(0, this.getHeight(), 0),
            () -> this.getPosition().add(0, this.getHeight(), this.getDepth()),
            () -> this.getPosition().add(this.getDimensions()),
            () -> this.getPosition().add(this.getWidth(), this.getHeight(), 0)
        )
        .withTextureUVs(
            () -> new Vector2f(this.getTextureOffsetX() + this.getDepth() / this.getTextureDensityX(), this.getTextureOffsetY()),
            () -> new Vector2f(this.getTextureOffsetX() + this.getDepth() / this.getTextureDensityX(), this.getTextureOffsetY() + this.getDepth() / this.getTextureDensityY()),
            () -> new Vector2f(this.getTextureOffsetX() + (this.getDepth() + this.getWidth()) / this.getTextureDensityX(), this.getTextureOffsetY() + this.getDepth() / this.getTextureDensityY()),
            () -> new Vector2f(this.getTextureOffsetX() + (this.getDepth() + this.getWidth()) / this.getTextureDensityX(), this.getTextureOffsetY())
        )
        .withColor(() -> this.color.get())
        .withLightMap(() -> this.lightMap.getAsInt())
        .build();

    this.bottom = vertexQuadBuilderFactory.create()
        .withVertices(
            () -> this.getPosition().add(0, 0, 0),
            () -> this.getPosition().add(0, 0, this.getDepth()),
            () -> this.getPosition().add(this.getWidth(), 0, this.getDepth()),
            () -> this.getPosition().add(this.getWidth(), 0, 0)
        )
        .withTextureUVs(
            () -> new Vector2f(getTextureOffsetX() + (getWidth() + getDepth()) / getTextureDensityX(), getTextureOffsetY()),
            () -> new Vector2f(getTextureOffsetX() + (getWidth() + getDepth()) / getTextureDensityX(), getTextureOffsetY() + getDepth() / getTextureDensityY()),
            () -> new Vector2f(getTextureOffsetX() + (getDepth() + getWidth() + getWidth()) / getTextureDensityX(), getTextureOffsetY() + getDepth() / getTextureDensityY()),
            () -> new Vector2f(getTextureOffsetX() + (getDepth() + getWidth() + getWidth()) / getTextureDensityX(), getTextureOffsetY())
        )
        .withColor(() -> this.color.get())
        .withLightMap(() -> this.lightMap.getAsInt())
        .build();
  }

  public Vector2f getTextureDensity() {
    return this.textureDensity.get();
  }

  public float getTextureDensityX() {
    return this.getTextureDensity().x;
  }

  public float getTextureDensityY() {
    return this.getTextureDensity().y;
  }

  public Vector2f getTextureOffset() {
    return this.textureOffset.get();
  }

  public float getTextureOffsetX() {
    return this.getTextureOffset().x;
  }

  public float getTextureOffsetY() {
    return this.getTextureOffset().y;
  }


  public Vector3f getPosition() {
    return this.position.get();
  }

  public Vector3f getDimensions() {
    return this.dimensions.get();
  }

  public float getHeight() {
    return this.getDimensions().y;
  }

  public float getDepth() {
    return this.getDimensions().z;
  }

  public float getWidth() {
    return this.getDimensions().x;
  }

  public float getPositionX() {
    return this.getPosition().x;
  }

  public float getPositionY() {
    return this.getPosition().y;
  }

  public float getPositionZ() {
    return this.getPosition().z;
  }

  public short getLightMapU() {
    return (short) ((this.getLightMap() >> (8 * 2)) & 0xff);
  }

  public short getLightMapV() {
    return (short) (this.getLightMap() & 0xff);
  }

  public int getLightMap() {
    return this.lightMap.getAsInt();
  }

  public VertexBox setPosition(Supplier<Vector3f> position) {
    this.position = position;
    return this;
  }

  public VertexBox setPosition(Vector3f position) {
    return this.setPosition(position.x, position.y, position.z);
  }

  public VertexBox setPosition(float x, float y, float z) {
    return this.setPosition(new Vector3f(x, y, z));
  }

  public VertexBox setDimensions(Supplier<Vector3f> dimensions) {
    this.dimensions = dimensions;
    return this;
  }

  public VertexBox setDimensions(Vector3f dimensions) {
    return this.setDimensions(dimensions.x, dimensions.y, dimensions.z);
  }

  public VertexBox setDimensions(float x, float y, float z) {
    return this.setDimensions(new Vector3f(x, y, z));
  }

  public VertexBox setHeight(float height) {
    this.setDimensions(() -> {
      Vector3f dimensions = this.getDimensions();
      dimensions.y = height;
      return dimensions;
    });
    return this;
  }

  public VertexBox setDepth(float depth) {
    this.setDimensions(() -> {
      Vector3f dimensions = this.getDimensions();
      dimensions.z = depth;
      return dimensions;
    });
    return this;
  }

  public VertexBox setWidth(float width) {
    this.setDimensions(() -> {
      Vector3f dimensions = this.getDimensions();
      dimensions.x = width;
      return dimensions;
    });
    return this;
  }

  public VertexBox setPositionX(float x) {
    this.setPosition(() -> {
      Vector3f dimensions = this.getPosition();
      dimensions.x = x;
      return dimensions;
    });
    return this;
  }

  public VertexBox setPositionY(float y) {
    this.setPosition(() -> {
      Vector3f dimensions = this.getPosition();
      dimensions.y = y;
      return dimensions;
    });
    return this;
  }

  public VertexBox setPositionZ(float z) {
    this.setPosition(() -> {
      Vector3f dimensions = this.getPosition();
      dimensions.z = z;
      return dimensions;
    });
    return this;
  }

  public VertexBox setLightMapV(short v) {
    this.setLightMapMasked((this.getLightMap() & 0xffff0000) | (v & 0x0000ffff));
    return this;
  }

  public VertexBox setLightMapU(short u) {
    this.setLightMapMasked((this.getLightMap() & 0x0000ffff) | (u & 0xffff0000));
    return this;
  }

  public VertexBox setLightMapMasked(int lightMapMasked) {
    this.lightMap = () -> lightMapMasked;
    return this;
  }

  public VertexBox render(MatrixStack matrixStack, VertexBuffer vertexBuffer) {
    this.back.render(matrixStack, vertexBuffer);
    this.front.render(matrixStack, vertexBuffer);
    this.right.render(matrixStack, vertexBuffer);
//    this.left.render(matrixStack, vertexBuffer);
    this.top.render(matrixStack, vertexBuffer);
//    this.bottom.render(matrixStack, vertexBuffer);
    return this;
  }

  @Implement(VertexBox.Builder.class)
  public static class BuilderImpl implements VertexBox.Builder {

    private final VertexQuad.Builder.Factory vertexQuadBuilderFactory;
    private final Supplier<Vector3f> position;
    private final Supplier<Vector3f> dimensions;
    private Supplier<Color> color = () -> null;
    private IntSupplier lightMap = () -> 0;
    private Supplier<Vector2f> textureOffset = () -> new Vector2f(0, 0);
    private Supplier<Vector2f> textureDensity = () -> new Vector2f(1, 1);

    @AssistedInject
    private BuilderImpl(
        VertexQuad.Builder.Factory vertexQuadBuilderFactory,
        @Assisted("position") Vector3f position,
        @Assisted("dimensions") Vector3f dimensions) {
      this(vertexQuadBuilderFactory, () -> position, () -> dimensions);
    }

    @AssistedInject
    private BuilderImpl(
        VertexQuad.Builder.Factory vertexQuadBuilderFactory,
        @Assisted("position") Supplier<Vector3f> position,
        @Assisted("dimensions") Supplier<Vector3f> dimensions) {
      this.vertexQuadBuilderFactory = vertexQuadBuilderFactory;
      this.position = position;
      this.dimensions = dimensions;
    }

    public Builder withColor(Color color) {
      return this.withColor(() -> color);
    }

    public Builder withColor(Supplier<Color> color) {
      this.color = color;
      return this;
    }

    public Builder withLightMap(int mask) {
      return this.withLightMap(() -> mask);
    }

    public Builder withLightMap(IntSupplier lightMap) {
      this.lightMap = lightMap;
      return this;
    }

    public Builder withTextureDensity(Supplier<Vector2f> textureDensity) {
      this.textureDensity = textureDensity;
      return this;
    }

    public Builder withTextureDensity(Vector2f textureDensity) {
      return this.withTextureDensity(() -> textureDensity);
    }

    public Builder withTextureOffset(Supplier<Vector2f> textureOffset) {
      this.textureOffset = textureOffset;
      return this;
    }

    public Builder withTextureOffset(Vector2f textureOffset) {
      return this.withTextureOffset(() -> textureOffset);
    }

    public VertexBox build() {
      return new VertexBoxImpl(this.position, this.dimensions, vertexQuadBuilderFactory, this.color, textureDensity, textureOffset, this.lightMap);
    }
  }

}
