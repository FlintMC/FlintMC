package net.labyfy.internal.component.render;

import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.MatrixStack;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexTriangle;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.function.Supplier;

public class VertexTriangleImpl implements VertexTriangle {

  private final Supplier<Vector3f>
      vertex1,
      vertex2,
      vertex3;

  private VertexTriangleImpl(Supplier<Vector3f> vertex1, Supplier<Vector3f> vertex2, Supplier<Vector3f> vertex3) {
    this.vertex1 = vertex1;
    this.vertex2 = vertex2;
    this.vertex3 = vertex3;
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

  public Vector3f[] getVertices() {
    return new Vector3f[]{
        this.getVertex1(), this.getVertex2(), this.getVertex3()
    };
  }

  public VertexTriangle render(MatrixStack matrixStack, VertexBuffer vertexBuffer) {
    matrixStack.push();
    vertexBuffer
        .pos(this.getVertex1())
        .end()
        .pos(this.getVertex2())
        .end()
        .pos(this.getVertex3())
        .end();
    matrixStack.pop();
    return this;
  }

  @Implement(Builder.class)
  public static class BuilderImpl implements Builder {

    private Supplier<Vector3f>
        vertex1,
        vertex2,
        vertex3;

    @AssistedInject
    private BuilderImpl() {
    }


    public Builder withVertices(Supplier<Vector3f> vertex1, Supplier<Vector3f> vertex2, Supplier<Vector3f> vertex3) {
      this.vertex1 = vertex1;
      this.vertex2 = vertex2;
      this.vertex3 = vertex3;
      return this;
    }

    public VertexTriangle build() {
      Objects.requireNonNull(this.vertex1);
      Objects.requireNonNull(this.vertex2);
      Objects.requireNonNull(this.vertex3);
      return new VertexTriangleImpl(this.vertex1, this.vertex2, this.vertex3);
    }
  }

}
