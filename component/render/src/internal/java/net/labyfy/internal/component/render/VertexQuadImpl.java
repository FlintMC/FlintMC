package net.labyfy.internal.component.render;

import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.MatrixStack;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexQuad;
import net.labyfy.component.render.VertexTriangle;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class VertexQuadImpl implements VertexQuad {

  private final Supplier<Vector3f>
      vertex1,
      vertex2,
      vertex3,
      vertex4;

  private final Supplier<Color> color;

  private final VertexTriangle
      triangle1,
      triangle2;

  private final Supplier<Vector2f>
      vertex1TextureUV,
      vertex2TextureUV,
      vertex3TextureUV,
      vertex4TextureUV;

  private final IntSupplier lightMap;

  private VertexQuadImpl(
      VertexTriangle.Builder.Factory vertexTriangleBuilderFactory,
      Supplier<Vector3f> vertex1,
      Supplier<Vector3f> vertex2,
      Supplier<Vector3f> vertex3,
      Supplier<Vector3f> vertex4,
      Supplier<Color> color,
      Supplier<Vector2f> vertex1TextureUV,
      Supplier<Vector2f> vertex2TextureUV,
      Supplier<Vector2f> vertex3TextureUV,
      Supplier<Vector2f> vertex4TextureUV,
      IntSupplier lightMap) {
    this.vertex1 = vertex1;
    this.vertex2 = vertex2;
    this.vertex3 = vertex3;
    this.vertex4 = vertex4;
    this.color = color;
    this.vertex1TextureUV = vertex1TextureUV;
    this.vertex2TextureUV = vertex2TextureUV;
    this.vertex3TextureUV = vertex3TextureUV;
    this.vertex4TextureUV = vertex4TextureUV;
    this.lightMap = lightMap;

    this.triangle1 = vertexTriangleBuilderFactory
        .create()
        .withVertices(
            this::getVertex1,
            this::getVertex2,
            this::getVertex3
        )
        .withColor(this::getColor)
        .withLightMap(this::getLightMap)
        .withTextureUVs(
            this::getVertex1TextureUV,
            this::getVertex2TextureUV,
            this::getVertex3TextureUV
        )
        .build();

    this.triangle2 = vertexTriangleBuilderFactory
        .create()
        .withVertices(
            this::getVertex1,
            this::getVertex3,
            this::getVertex4
        )
        .withColor(this::getColor)
        .withLightMap(this::getLightMap)
        .withTextureUVs(
            this::getVertex1TextureUV,
            this::getVertex3TextureUV,
            this::getVertex4TextureUV
        )
        .build();

  }

  public Vector3f getVertex1() {
    return this.vertex1.get();
  }

  public Vector3f getVertex2() {
    return this.vertex2.get();
  }

  public Vector3f getVertex3() {
    return this.vertex3.get();
  }

  public Vector3f getVertex4() {
    return this.vertex4.get();
  }

  public Vector3f[] getVertices() {
    return new Vector3f[]{this.getVertex1(), this.getVertex2(), this.getVertex3(), this.getVertex4()};
  }

  public Vector2f getVertex1TextureUV() {
    return vertex1TextureUV.get();
  }

  public Vector2f getVertex2TextureUV() {
    return vertex2TextureUV.get();
  }

  public Vector2f getVertex3TextureUV() {
    return vertex3TextureUV.get();
  }

  public Vector2f getVertex4TextureUV() {
    return vertex4TextureUV.get();
  }

  public Vector2f[] getVertexTextureUVs() {
    return new Vector2f[]{this.getVertex1TextureUV(), this.getVertex2TextureUV(), this.getVertex3TextureUV(), this.getVertex4TextureUV()};
  }

  public Color getColor() {
    return this.color.get();
  }

  public int getLightMap() {
    return this.lightMap.getAsInt();
  }

  public VertexQuad render(MatrixStack matrixStack, VertexBuffer vertexBuffer) {
    this.triangle1.render(matrixStack, vertexBuffer);
    this.triangle2.render(matrixStack, vertexBuffer);
    return this;
  }

  @Implement(VertexQuad.Builder.class)
  public static class BuilderImpl implements VertexQuad.Builder {

    private final VertexTriangle.Builder.Factory vertexTriangleBuilderFactory;
    private Supplier<Vector3f>
        vertex1,
        vertex2,
        vertex3,
        vertex4;

    private Supplier<Color> color = () -> null;

    private Supplier<Vector2f>
        vertex1TextureUV = () -> null,
        vertex2TextureUV = () -> null,
        vertex3TextureUV = () -> null,
        vertex4TextureUV = () -> null;

    private IntSupplier lightMap = () -> 0;

    @AssistedInject
    private BuilderImpl(VertexTriangle.Builder.Factory vertexTriangleBuilderFactory) {
      this.vertexTriangleBuilderFactory = vertexTriangleBuilderFactory;
    }

    public Builder withVertices(Supplier<Vector3f> vertex1, Supplier<Vector3f> vertex2, Supplier<Vector3f> vertex3, Supplier<Vector3f> vertex4) {
      this.vertex1 = vertex1;
      this.vertex2 = vertex2;
      this.vertex3 = vertex3;
      this.vertex4 = vertex4;
      return this;
    }

    public Builder withVertices(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3, Vector3f vertex4) {
      return this.withVertices(() -> new Vector3f(vertex1), () -> new Vector3f(vertex2), () -> new Vector3f(vertex3), () -> new Vector3f(vertex4));
    }

    public Builder withTextureUVs(Supplier<Vector2f> vertex1TextureUV, Supplier<Vector2f> vertex2TextureUV, Supplier<Vector2f> vertex3TextureUV, Supplier<Vector2f> vertex4TextureUV) {
      this.vertex1TextureUV = vertex1TextureUV;
      this.vertex2TextureUV = vertex2TextureUV;
      this.vertex3TextureUV = vertex3TextureUV;
      this.vertex4TextureUV = vertex4TextureUV;
      return this;
    }


    public Builder withTextureUVs(Vector2f vertex1TextureUV, Vector2f vertex2TextureUV, Vector2f vertex3TextureUV, Vector2f vertex4TextureUV) {
      return this.withTextureUVs(() -> new Vector2f(vertex1TextureUV), () -> new Vector2f(vertex2TextureUV), () -> new Vector2f(vertex3TextureUV), () -> new Vector2f(vertex4TextureUV));
    }

    public Builder withColor(Supplier<Color> color) {
      this.color = color;
      return this;
    }

    public Builder withColor(Color color) {
      return this.withColor(() -> color);
    }

    public VertexQuad.Builder withLightMap(int lightMap) {
      return this.withLightMap(() -> lightMap);
    }

    public VertexQuad.Builder withLightMap(IntSupplier lightMap) {
      this.lightMap = lightMap;
      return this;
    }

    public VertexQuad build() {
      Objects.requireNonNull(vertex1);
      Objects.requireNonNull(vertex2);
      Objects.requireNonNull(vertex3);
      Objects.requireNonNull(vertex4);
      return new VertexQuadImpl(vertexTriangleBuilderFactory, vertex1, vertex2, vertex3, vertex4, color, vertex1TextureUV, vertex2TextureUV, vertex3TextureUV, vertex4TextureUV, lightMap);
    }
  }

}
