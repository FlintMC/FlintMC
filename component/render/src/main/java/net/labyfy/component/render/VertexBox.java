package net.labyfy.component.render;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Represents a box in the 3 dimensional coordinate space.
 * Utility class to render boxes more easily and match the minecraft structure better.
 */
public interface VertexBox {

  /**
   * The texture density describes how many pixels of texture will be mapped to 1 unit of dimensions.
   *
   * @return The texture density of the box
   */
  Vector2f getTextureDensity();

  /**
   * The texture density describes how many pixels of texture will be mapped to 1 unit of dimensions.
   *
   * @return The x componente from the texture density of the box
   */
  float getTextureDensityX();

  /**
   * The texture density describes how many pixels of texture will be mapped to 1 unit of dimensions.
   *
   * @return The y componente from the texture density of the box
   */
  float getTextureDensityY();


  /**
   * The texture uv offset in %.
   *
   * @return The texture uv offset of the box
   */
  Vector2f getTextureOffset();

  /**
   * The texture uv offset in %.
   *
   * @return The x componente from the texture uv offset of the box
   */
  float getTextureOffsetX();

  /**
   * The texture uv offset in %.
   *
   * @return The y componente from the texture uv offset of the box
   */
  float getTextureOffsetY();

  /**
   * @return the local transformation matrix of the box
   */
  Matrix4f getTransform();

  /**
   * @return the position of the box
   */
  Vector3f getPosition();

  /**
   * Sets the position of the box.
   *
   * @param position the target position to set the box to
   * @return this
   */
  VertexBox setPosition(Supplier<Vector3f> position);

  /**
   * Sets the position of the box.
   *
   * @param position the target position to set the box to
   * @return this
   */
  VertexBox setPosition(Vector3f position);

  /**
   * Sets the local transformation matrix of this box
   *
   * @param transform the world matrix
   * @return this
   */
  VertexBox setTransform(Supplier<Matrix4f> transform);

  /**
   * Sets the local transformation matrix of this box
   *
   * @param transform the world matrix
   * @return this
   */
  VertexBox setTransform(Matrix4f transform);


  /**
   * @return the dimensions of the box
   */
  Vector3f getDimensions();

  /**
   * Sets the dimensions of the box.
   *
   * @param dimensions the target dimensions to set the box to
   * @return this
   */
  VertexBox setDimensions(Supplier<Vector3f> dimensions);

  /**
   * Sets the dimensions of the box.
   *
   * @param dimensions the target dimensions to set the box to
   * @return this
   */
  VertexBox setDimensions(Vector3f dimensions);

  /**
   * @return the height of the box
   */
  float getHeight();

  /**
   * Sets the height of the box
   *
   * @param height the target height to set the box to
   * @return this
   */
  VertexBox setHeight(float height);

  /**
   * @return the depth of the box
   */
  float getDepth();

  /**
   * Sets the depth of the box
   *
   * @param depth the target depth to set the box to
   * @return this
   */
  VertexBox setDepth(float depth);

  /**
   * @return the width of the box
   */
  float getWidth();

  /**
   * Sets the width of the box
   *
   * @param width the target width to set the box to
   * @return this
   */
  VertexBox setWidth(float width);

  /**
   * @return the x position of the box
   */
  float getPositionX();

  /**
   * Sets the x position of the box
   *
   * @param x the target x position to set the box to
   * @return this
   */
  VertexBox setPositionX(float x);

  /**
   * @return the y position of the box
   */
  float getPositionY();

  /**
   * Sets the y position of the box
   *
   * @param y the target y position to set the box to
   * @return this
   */
  VertexBox setPositionY(float y);

  /**
   * @return the z position of the box
   */
  float getPositionZ();

  /**
   * Sets the z position of the box
   *
   * @param z the target z position to set the box to
   * @return this
   */
  VertexBox setPositionZ(float z);

  /**
   * @return the u lightmap coordinate of the box
   */
  short getLightMapU();

  /**
   * Sets the u lightmap coordinate of the box
   *
   * @param u the target  u lightmap coordinate to set the box to
   * @return this
   */
  VertexBox setLightMapU(short u);

  /**
   * @return the v lightmap coordinate of the box
   */
  short getLightMapV();

  /**
   * Sets the v lightmap coordinate of the box
   *
   * @param v the target  v lightmap coordinate to set the box to
   * @return this
   */
  VertexBox setLightMapV(short v);

  /**
   * @return the lightmap coordinates
   */
  int getLightMap();

  /**
   * Sets the position of the box.
   *
   * @param x the target x position to set the box to
   * @param y the target y position to set the box to
   * @param z the target z position to set the box to
   * @return this
   */
  VertexBox setPosition(float x, float y, float z);

  /**
   * Sets the dimensions of the box.
   *
   * @param x the target x dimensions to set the box to
   * @param y the target y dimensions to set the box to
   * @param z the target z dimensions to set the box to
   * @return this
   */
  VertexBox setDimensions(float x, float y, float z);

  /**
   * Sets the lightmap coordinates of the box.
   * First 2 bits describe v, last 2 bits describe u
   *
   * @param lightMap the combined lightmap.
   * @return this
   */
  VertexBox setLightMapMasked(int lightMap);

  /**
   * Sets the color of the box.
   *
   * @param color the color to set the box to
   * @return this
   */
  VertexBox setColor(Color color);

  /**
   * Sets the color of the box.
   *
   * @param color the color to set the box to
   * @return this
   */
  VertexBox setColor(Supplier<Color> color);

  /**
   * @return the parent of this box
   */
  VertexBox getParent();

  /**
   * Sets the parent of this box.
   *
   * @param vertexBox the parent
   * @return this
   */
  VertexBox setParent(VertexBox parent);

  /**
   * Sets the parent of this box.
   *
   * @param vertexBox the parent
   * @return this
   */
  VertexBox setParent(Supplier<VertexBox> parent);

  /**
   * Renders the box to the target {@link VertexBuffer} and {@link MatrixStack}.
   *
   * @param matrixStack  the world matrix to render the box to
   * @param vertexBuffer the vertex buffer to add the vertices to
   * @return this
   */
  VertexBox render(MatrixStack matrixStack, VertexBuffer vertexBuffer);

  interface Builder {

    Builder withColor(Color color);

    Builder withColor(Supplier<Color> color);

    Builder withLightMap(int mask);

    Builder withLightMap(IntSupplier lightmap);

    Builder withTextureDensity(Supplier<Vector2f> textureDensity);

    Builder withTextureDensity(Vector2f textureDensity);

    Builder withTextureOffset(Supplier<Vector2f> textureOffset);

    Builder withTextureOffset(Vector2f textureOffset);

    VertexBox build();

    @AssistedFactory(Builder.class)
    interface Factory {
      Builder create(@Assisted("position") Vector3f position, @Assisted("dimensions") Vector3f dimensions);

      Builder create(@Assisted("position") Supplier<Vector3f> position, @Assisted("dimensions") Supplier<Vector3f> dimensions);
    }

  }


}
