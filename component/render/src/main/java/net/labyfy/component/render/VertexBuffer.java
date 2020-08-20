package net.labyfy.component.render;

import org.joml.*;

import java.awt.*;

/**
 * This class represents a collection of vertices and how they should be rendered.
 * <p>
 * This is necessary because Minecraft starts to use batched rendering in the 1.15.2 and in order to maintain a cross version
 * rendering module, batched rendering had to be implemented.
 * <p>
 * This class is quite similar to the good old BufferBuilder/IVertexConsumer.
 * <p>
 * See also at {@link MatrixStack}
 */
public interface VertexBuffer {

  /**
   * Sets the position of the current vertex.
   *
   * @param x position to set the current vertex to
   * @param y position to set the current vertex to
   * @param z position to set the current vertex to
   * @return this
   */
  VertexBuffer pos(float x, float y, float z);

  /**
   * Sets the position of the current vertex.
   *
   * @param x x position to set the current vertex to
   * @param y y position to set the current vertex to
   * @param z z position to set the current vertex to
   * @return this
   */
  VertexBuffer pos(Vector3f position);

  /**
   * Sets the color of the current vertex.
   *
   * @param r     red component of the color to set the vertex to
   * @param g     green component of the color to set the vertex to
   * @param b     blue component of the color to set the vertex to
   * @param alpha alpha component of the color to set the current vertex to
   * @return this
   */
  VertexBuffer color(int r, int g, int b, int alpha);

  /**
   * Sets the color of the current vertex.
   *
   * @param color color to set the vertex to
   * @return this
   */
  VertexBuffer color(Color color);

  /**
   * Sets the normal of the current vertex.
   *
   * @param x x normal to set the current vertex to
   * @param y y normal to set the current vertex to
   * @param z z normal to set the current vertex to
   * @return this
   */
  VertexBuffer normal(byte x, byte y, byte z);

  /**
   * Sets the normal of the current vertex.
   *
   * @param normal normal to set the current vertex to
   * @return this
   */
  VertexBuffer normal(Vector3i normal);

  /**
   * Marks the current vertex as finished and prepare the context for the next vertex.
   *
   * @return this
   */
  VertexBuffer end();

  /**
   * Sets the lightmap coordinates of the current vertex.
   *
   * @param u u lightmap coordinate to set the current vertex to
   * @param v v lightmap coordinate to set the current vertex to
   * @return this
   */
  VertexBuffer lightmap(short u, short v);

  /**
   * Sets the lightmap coordinates of the current vertex.
   * First 2 bytes represent v, last 2 bytes represent u.
   *
   * @param masked the lightmap coordinates to set the current vertex to
   * @return this
   */
  VertexBuffer lightmap(int masked);

  /**
   * Sets the texture uv coordinates of current vertex
   *
   * @param u the texture u coordinate to set the current vertex to
   * @param v the texture v coordinate to set the current vertex to
   * @return this
   */
  VertexBuffer texture(float u, float v);

  /**
   * Sets the texture uv coordinates of current vertex
   *
   * @param texture the texture coordinates to set the current vertex to
   * @return this
   */
  VertexBuffer texture(Vector2f texture);

  /**
   * @return the amount of written vertices
   */
  int getVertexCount();

  /**
   * A {@link VertexFormat} defines which properties can be set to a vertex.
   *
   * @return the vertex format of the current vertex
   */
  VertexFormat getFormat();

  /**
   * @return the current world transformation matrix
   */
  Matrix4f getWorldContext();

  /**
   * Sets the current world transformation matrix.
   * Should not be modified in most cases.
   *
   * @param matrix the matrix to set the world transformation to
   * @return this
   */
  VertexBuffer setWorldContext(Matrix4f matrix);

  /**
   * @return the current world transformation matrix
   */
  Matrix3f getNormalContext();

  /**
   * Sets the current world normal matrix.
   * Should not be modified in most cases.
   *
   * @param normalContext the matrix to set the world normal transformation to
   * @return this
   */
  VertexBuffer setNormalContext(Matrix3f normalContext);

  /**
   * The more advanced vertex buffer should only be used if there is a need for it.
   * Most tasks should be possible with the standard {@link VertexBuffer}.
   *
   * @return the more advanced vertex buffer
   */
  AdvancedVertexBuffer advanced();

}
