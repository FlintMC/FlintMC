package net.labyfy.internal.component.render;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.Vertex;
import net.labyfy.component.render.VertexBuffer;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.awt.*;

@Implement(Vertex.class)
public class VertexImpl implements Vertex {

  private Vector3f position;
  private Vector3f normal = new Vector3f(0, 0, 0);
  private Vector2f textureUV = new Vector2f(0, 0);
  private Vector2i overlayUV = new Vector2i(0, 10);
  private Vector2i lightingUV = new Vector2i(0, 0);
  private Color color = Color.WHITE;

  @AssistedInject
  private VertexImpl(@Assisted("position") Vector3f position) {
    this.position = new Vector3f(position);
  }

  @AssistedInject
  private VertexImpl(@Assisted("x") float positionX, @Assisted("y") float positionY, @Assisted("z") float positionZ) {
    this(new Vector3f(positionX, positionY, positionZ));
  }

  /**
   * {@inheritDoc}
   */
  public Vector3f getPosition() {
    return this.position;
  }

  /**
   * {@inheritDoc}
   */
  public Vector2f getTextureUV() {
    return this.textureUV;
  }

  /**
   * {@inheritDoc}
   */
  public Vector3f getNormal() {
    return this.normal;
  }

  /**
   * {@inheritDoc}
   */
  public Vector2i getOverlayUV() {
    return this.overlayUV;
  }

  /**
   * {@inheritDoc}
   */
  public Vector2i getLightingUV() {
    return this.lightingUV;
  }

  /**
   * {@inheritDoc}
   */
  public Color getColor() {
    return this.color;
  }

  /**
   * {@inheritDoc}
   */
  public Vertex setPosition(float x, float y, float z) {
    this.position.x = x;
    this.position.y = y;
    this.position.z = z;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Vertex setPosition(Vector3f position) {
    this.setPosition(position.x, position.y, position.z);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Vertex setTextureUV(float u, float v) {
    this.textureUV.x = u;
    this.textureUV.y = v;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Vertex setTextureUV(Vector2f textureUV) {
    this.setTextureUV(textureUV.x, textureUV.y);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Vertex setColor(Color color) {
    this.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Vertex setColor(int red, int green, int blue) {
    this.setColor(red, green, blue, 255);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Vertex setColor(int red, int green, int blue, int alpha) {
    this.color = new Color(red, green, blue, alpha);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Vertex setLightmapUV(Vector2i lightmap) {
    this.lightingUV.set(lightmap);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Vertex setLightmapUV(int masked) {
    this.lightingUV.set(masked & 0xff, masked >>> 16);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Vertex setLightmapUV(short u, short v) {
    this.setLightmapUV(((((int) v) & 0xfff) << 16) | ((int) u));
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Vertex setNormal(float x, float y, float z) {
    this.normal.x = x;
    this.normal.y = y;
    this.normal.z = z;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public Vertex setNormal(Vector3f normal) {
    return this.setNormal(normal.x, normal.y, normal.z);
  }

  /**
   * {@inheritDoc}
   */
  public Vertex render(VertexBuffer vertexBuffer) {
    vertexBuffer
        .pos(this.getPosition())
        .normal(this.getNormal())
        .color(this.getColor())
        .lightmap((short) this.getLightingUV().x, (short) this.getLightingUV().y)
        .texture(this.getTextureUV())
        .overlay(this.getOverlayUV())
        .end();
    return this;
  }
}
