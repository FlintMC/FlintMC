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

  //all sides of the box
  private final VertexQuad back;
  private final VertexQuad front;
  private final VertexQuad right;
  private final VertexQuad left;
  private final VertexQuad top;
  private final VertexQuad bottom;

  //all properties of the box
  private Supplier<Vector3f> position;
  private Supplier<Vector3f> dimensions;
  private Supplier<Color> color;
  private Supplier<Vector2f> textureDensity;
  private Supplier<Vector2f> textureOffset;
  private IntSupplier lightMap;

  /**
   * Constructs a {@link VertexQuadImpl}.
   * Should never be called by user.
   * See at {@link VertexQuad.Builder}
   *
   * @param position                 The position where the box should be rendered
   * @param dimensions               The dimensions of the box
   * @param vertexQuadBuilderFactory Factory to build {@link VertexQuad}. Will be injected by DI
   * @param color                    The color of the box
   * @param textureDensity           The texture density of the box. Describes how many pixels of texture will be mapped to 1 unit of dimensions
   * @param textureOffset            The texture uv offset in %
   * @param lightMap                 The lightmap of the bux
   */
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
        .withColor(() -> this.color.get())
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

  /**
   * {@inheritDoc}
   */
  public Vector2f getTextureDensity() {
    return this.textureDensity.get();
  }

  /**
   * {@inheritDoc}
   */
  public float getTextureDensityX() {
    return this.getTextureDensity().x;
  }

  /**
   * {@inheritDoc}
   */
  public float getTextureDensityY() {
    return this.getTextureDensity().y;
  }


  /**
   * {@inheritDoc}
   */
  public Vector2f getTextureOffset() {
    return this.textureOffset.get();
  }

  /**
   * {@inheritDoc}
   */
  public float getTextureOffsetX() {
    return this.getTextureOffset().x;
  }

  /**
   * {@inheritDoc}
   */
  public float getTextureOffsetY() {
    return this.getTextureOffset().y;
  }

  /**
   * {@inheritDoc}
   */
  public Vector3f getPosition() {
    return this.position.get();
  }

  /**
   * {@inheritDoc}
   */
  public Vector3f getDimensions() {
    return this.dimensions.get();
  }

  /**
   * {@inheritDoc}
   */
  public float getHeight() {
    return this.getDimensions().y;
  }

  /**
   * {@inheritDoc}
   */
  public float getDepth() {
    return this.getDimensions().z;
  }


  /**
   * {@inheritDoc}
   */
  public float getWidth() {
    return this.getDimensions().x;
  }

  /**
   * {@inheritDoc}
   */
  public float getPositionX() {
    return this.getPosition().x;
  }

  /**
   * {@inheritDoc}
   */
  public float getPositionY() {
    return this.getPosition().y;
  }


  /**
   * {@inheritDoc}
   */
  public float getPositionZ() {
    return this.getPosition().z;
  }


  /**
   * {@inheritDoc}
   */
  public short getLightMapU() {
    return (short) ((this.getLightMap() >> (8 * 2)) & 0xff);
  }

  /**
   * {@inheritDoc}
   */
  public short getLightMapV() {
    return (short) (this.getLightMap() & 0xff);
  }

  /**
   * {@inheritDoc}
   */
  public int getLightMap() {
    return this.lightMap.getAsInt();
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setPosition(Supplier<Vector3f> position) {
    this.position = position;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setPosition(Vector3f position) {
    return this.setPosition(position.x, position.y, position.z);
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setPosition(float x, float y, float z) {
    return this.setPosition(new Vector3f(x, y, z));
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setDimensions(Supplier<Vector3f> dimensions) {
    this.dimensions = dimensions;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setDimensions(Vector3f dimensions) {
    return this.setDimensions(dimensions.x, dimensions.y, dimensions.z);
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setDimensions(float x, float y, float z) {
    return this.setDimensions(new Vector3f(x, y, z));
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setHeight(float height) {
    this.setDimensions(() -> {
      Vector3f dimensions = this.getDimensions();
      dimensions.y = height;
      return dimensions;
    });
    return this;
  }


  /**
   * {@inheritDoc}
   */
  public VertexBox setDepth(float depth) {
    this.setDimensions(() -> {
      Vector3f dimensions = this.getDimensions();
      dimensions.z = depth;
      return dimensions;
    });
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setWidth(float width) {
    this.setDimensions(() -> {
      Vector3f dimensions = this.getDimensions();
      dimensions.x = width;
      return dimensions;
    });
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setPositionX(float x) {
    this.setPosition(() -> {
      Vector3f dimensions = this.getPosition();
      dimensions.x = x;
      return dimensions;
    });
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setPositionY(float y) {
    this.setPosition(() -> {
      Vector3f dimensions = this.getPosition();
      dimensions.y = y;
      return dimensions;
    });
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setPositionZ(float z) {
    this.setPosition(() -> {
      Vector3f dimensions = this.getPosition();
      dimensions.z = z;
      return dimensions;
    });
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setLightMapV(short v) {
    this.setLightMapMasked((this.getLightMap() & 0xffff0000) | (v & 0x0000ffff));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setLightMapU(short u) {
    this.setLightMapMasked((this.getLightMap() & 0x0000ffff) | (u & 0xffff0000));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox setLightMapMasked(int lightMap) {
    this.lightMap = () -> lightMap;
    return this;
  }


  /**
   * {@inheritDoc}
   */
  public VertexBox setColor(Color color) {
    return this.setColor(() -> color);
  }


  /**
   * {@inheritDoc}
   */
  public VertexBox setColor(Supplier<Color> color) {
    this.color = color;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexBox render(MatrixStack matrixStack, VertexBuffer vertexBuffer) {
    this.back.render(matrixStack, vertexBuffer);
    this.front.render(matrixStack, vertexBuffer);
    this.right.render(matrixStack, vertexBuffer);
    this.left.render(matrixStack, vertexBuffer);
    this.top.render(matrixStack, vertexBuffer);
    this.bottom.render(matrixStack, vertexBuffer);
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
      this(vertexQuadBuilderFactory, () -> new Vector3f(position), () -> new Vector3f(dimensions));
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
      return new VertexBoxImpl(this.position, this.dimensions, this.vertexQuadBuilderFactory, this.color, this.textureDensity, this.textureOffset, this.lightMap);
    }
  }

}
