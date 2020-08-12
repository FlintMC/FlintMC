package net.labyfy.internal.component.render;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.MatrixStack;
import net.labyfy.component.render.VertexBox;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexQuad;
import org.joml.Vector3f;

@Implement(VertexBox.class)
public class VertexBoxImpl implements VertexBox {

  private final VertexQuad back;
  private final VertexQuad front;
  /* right,
   left,
   top,
   bottom,
   front,
   back;
   */
  private final Vector3f position;
  private final Vector3f dimensions;
  private int lightMapMasked;

  @AssistedInject
  private VertexBoxImpl(
      @Assisted("position") Vector3f position,
      @Assisted("dimensions") Vector3f dimensions,
      VertexQuad.Builder.Factory vertexQuadBuilderFactory) {

    this.position = new Vector3f(position);
    this.dimensions = new Vector3f(dimensions);

    this.back = vertexQuadBuilderFactory.create()
        .withVertices(
            this::getPosition,
            () -> this.getPosition().add(this.getWidth(), 0, 0),
            () -> this.getPosition().add(this.getWidth(), this.getHeight(), 0),
            () -> this.getPosition().add(0, this.getHeight(), 0)
        )
        .build();

    this.front = vertexQuadBuilderFactory.create()
        .withVertices(
            this::getPosition,
            () -> this.getPosition().add(this.getWidth(), 0, this.getDepth()),
            () -> this.getPosition().add(this.getWidth(), this.getHeight(), this.getDepth()),
            () -> this.getPosition().add(0, this.getHeight(), this.getDepth())
        )
        .build();


  }

  public Vector3f getPosition() {
    return new Vector3f(this.position);
  }

  public Vector3f getDimensions() {
    return new Vector3f(this.dimensions);
  }

  public float getHeight() {
    return this.dimensions.y;
  }

  public float getDepth() {
    return this.dimensions.z;
  }

  public float getWidth() {
    return this.dimensions.x;
  }

  public float getPositionX() {
    return this.position.x;
  }

  public float getPositionY() {
    return this.position.y;
  }

  public float getPositionZ() {
    return this.position.z;
  }

  public short getLightMapU() {
    return (short) ((this.lightMapMasked >> (8 * 2)) & 0xff);
  }

  public short getLightMapV() {
    return (short) (this.lightMapMasked & 0xff);
  }

  public int getLightMapMasked() {
    return this.lightMapMasked;
  }

  public VertexBox setPosition(Vector3f position) {
    return this.setPosition(position.x, position.y, position.z);
  }

  public VertexBox setPosition(float x, float y, float z) {
    this.position.x = x;
    this.position.y = y;
    this.position.z = z;
    return this;
  }

  public VertexBox setDimensions(Vector3f dimensions) {
    return this.setDimensions(dimensions.x, dimensions.y, dimensions.z);
  }

  public VertexBox setDimensions(float x, float y, float z) {
    this.dimensions.x = x;
    this.dimensions.y = y;
    this.dimensions.z = z;
    return this;
  }

  public VertexBox setHeight(float height) {
    this.dimensions.y = height;
    return this;
  }

  public VertexBox setDepth(float depth) {
    this.dimensions.z = depth;
    return this;
  }

  public VertexBox setWidth(float width) {
    this.dimensions.x = width;
    return this;
  }

  public VertexBox setPositionX(float x) {
    this.position.x = x;
    return this;
  }

  public VertexBox setPositionY(float y) {
    this.position.y = y;
    return this;
  }

  public VertexBox setPositionZ(float z) {
    this.position.z = z;
    return this;
  }

  public VertexBox setLightMapV(short v) {
    this.setLightMapMasked((this.getLightMapMasked() & 0xff00) | (v & 0x00ff));
    return this;
  }

  public VertexBox setLightMapU(short u) {
    this.setLightMapMasked((this.getLightMapMasked() & 0x00ff) | (u & 0xff00));
    return this;
  }

  public VertexBox setLightMapMasked(int lightMapMasked) {
    this.lightMapMasked = lightMapMasked;
    return this;
  }

  public VertexBox render(MatrixStack matrixStack, VertexBuffer vertexBuffer) {
    this.back.render(matrixStack, vertexBuffer);
    return this;
  }

}
