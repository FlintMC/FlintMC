package net.labyfy.internal.component.render;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.*;

@Implement(VertexQuad.class)
public class VertexQuadImpl implements VertexQuad {

  private final VertexTriangle
      triangle1,
      triangle2;

  @AssistedInject
  private VertexQuadImpl(
      @Assisted("vertex1") Vertex vertex1,
      @Assisted("vertex2") Vertex vertex2,
      @Assisted("vertex3") Vertex vertex3,
      @Assisted("vertex4") Vertex vertex4,
      VertexTriangle.Factory vertexTriangleFactory
  ) {

    this.triangle1 = vertexTriangleFactory.create(
        vertex1,
        vertex2,
        vertex3
    );
    this.triangle2 = vertexTriangleFactory.create(
        vertex1,
        vertex3,
        vertex4
    );

  }

  public VertexQuad render(MatrixStack matrixStack, VertexBuffer vertexBuffer) {
    this.triangle1.render(matrixStack, vertexBuffer);
    this.triangle2.render(matrixStack, vertexBuffer);
    return this;
  }

  public Vertex[] getVertices() {
    Vertex[] vertices1 = this.triangle1.getVertices();
    Vertex[] vertices2 = this.triangle2.getVertices();
    return new Vertex[]{vertices1[0], vertices1[1], vertices1[2], vertices2[3]};
  }
}
