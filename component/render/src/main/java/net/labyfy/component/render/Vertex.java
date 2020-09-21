package net.labyfy.component.render;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.awt.*;

/**
 * Represents a vertex in a 3d coordinate space.
 */
public interface Vertex {

  /**
   * @return the position of this vertex
   */
  Vector3f getPosition();

  /**
   * Sets the position of this vertex.
   *
   * @param position the position to set the vertex to
   * @return
   */
  Vertex setPosition(Vector3f position);

  /**
   * @return the texture uv coordinates of this vertex. Will be in the range between 0-1
   */
  Vector2f getTextureUV();

  /**
   * Sets the texture uv coordinate of this vertex
   *
   * @param textureUV the texture uv coordinates to set the vertex to
   * @return this
   */
  Vertex setTextureUV(Vector2f textureUV);

  /**
   * @return the normal of this vertex
   */
  Vector3f getNormal();

  /**
   * Sets the normal of this vertex
   *
   * @param normal the normal to set the vertex to
   * @return this
   */
  Vertex setNormal(Vector3f normal);

  /**
   * @return the overlay uv coordinates of this vertex. Will be in the range between 0-1
   */
  Vector2i getOverlayUV();

  /**
   * @return the lighting uv coordinate of this vertex. Will be in the range between 0-1
   */
  Vector2i getLightingUV();

  /**
   * @return the color of this vertex
   */
  Color getColor();

  /**
   * Sets the color of this vertex
   *
   * @param color the vertex to set the color to
   * @return this
   */
  Vertex setColor(Color color);

  /**
   * Sets the position of this vertex.
   *
   * @param x the x coordinate to set the position to
   * @param y the y coordinate to set the position to
   * @param z the z coordinate to set the position to
   * @return
   */
  Vertex setPosition(float x, float y, float z);

  /**
   * Sets the texture uv coordinates of this vertex
   *
   * @param u the u coordinate to set the texture uv to
   * @param v the v coordinate to set the texture uv to
   * @return this
   */
  Vertex setTextureUV(float u, float v);

  /**
   * Sets the color of this vertex
   *
   * @param red   the red color component to set the vertex color to
   * @param green the green color component to set the vertex color to
   * @param blue  the blue color component to set the vertex color to
   * @return this
   */
  Vertex setColor(int red, int green, int blue);

  /**
   * Sets the color of this vertex
   *
   * @param red   the red color component to set the vertex color to
   * @param green the green color component to set the vertex color to
   * @param blue  the blue color component to set the vertex color to
   * @param alpha the alpha color component to set the vertex color to
   * @return this
   */
  Vertex setColor(int red, int green, int blue, int alpha);

  /**
   * Sets the lightmap uv coordinates of this vertex
   *
   * @param lightmap x is u, y is v
   * @return
   */
  Vertex setLightmapUV(Vector2i lightmap);

  /**
   * Sets the lightmap uv coordinates of this vertex
   *
   * @param masked first 2 bytes are v, last 2 bytes are u
   * @return
   */
  Vertex setLightmapUV(int masked);

  /**
   * Sets the lightmap uv coordinates of this vertex
   *
   * @param u u coordinate to set the lightmap of this vertex to
   * @param v v coordinate to set the lightmap of this vertex to
   * @return
   */
  Vertex setLightmapUV(short u, short v);

  /**
   * Sets the normal of this vertex
   *
   * @param x x factor to set the normal of this vertex to
   * @param y y factor to set the normal of this vertex to
   * @param z z factor to set the normal of this vertex to
   * @return this
   */
  Vertex setNormal(float x, float y, float z);

  /**
   * Renders this vertex to a given 3d context
   *
   * @param matrixStack  the world context to render into
   * @param vertexBuffer the vertex data to render into
   * @return this
   */
  Vertex render(MatrixStack matrixStack, VertexBuffer vertexBuffer);

  @AssistedFactory(Vertex.class)
  interface Factory {
    Vertex create(@Assisted("position") Vector3f position);

    Vertex create(@Assisted("x") float positionX, @Assisted("y") float positionY, @Assisted("z") float positionZ);

  }
}
