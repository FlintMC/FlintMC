package net.labyfy.internal.component.render;

import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.MatrixStack;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexQuad;
import net.labyfy.component.render.VertexTriangle;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.function.Supplier;

public class VertexQuadImpl implements VertexQuad {

  private final Supplier<Vector3f>
      vertex1,
      vertex2,
      vertex3,
      vertex4;

  private final VertexTriangle
      triangle1,
      triangle2;

  private VertexQuadImpl(
      VertexTriangle.Builder.Factory vertexTriangleBuilderFactory,
      Supplier<Vector3f> vertex1,
      Supplier<Vector3f> vertex2,
      Supplier<Vector3f> vertex3,
      Supplier<Vector3f> vertex4
  ) {
    this.vertex1 = vertex1;
    this.vertex2 = vertex2;
    this.vertex3 = vertex3;
    this.vertex4 = vertex4;

    this.triangle1 = vertexTriangleBuilderFactory
        .create()
        .withVertices(
            this::getVertex1,
            this::getVertex2,
            this::getVertex3
        )
        .build();

    this.triangle2 = vertexTriangleBuilderFactory
        .create()
        .withVertices(
            this::getVertex1,
            this::getVertex3,
            this::getVertex4
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

    public VertexQuad build() {
      Objects.requireNonNull(vertex1);
      Objects.requireNonNull(vertex2);
      Objects.requireNonNull(vertex3);
      Objects.requireNonNull(vertex4);
      return new VertexQuadImpl(vertexTriangleBuilderFactory, vertex1, vertex2, vertex3, vertex4);
    }
  }

}
