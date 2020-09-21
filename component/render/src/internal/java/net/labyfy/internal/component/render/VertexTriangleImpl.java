package net.labyfy.internal.component.render;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.MatrixStack;
import net.labyfy.component.render.Vertex;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexTriangle;

import java.util.Arrays;

@Implement(VertexTriangle.class)
public class VertexTriangleImpl implements VertexTriangle {

  private final Vertex[] vertices = new Vertex[3];

  @AssistedInject
  private VertexTriangleImpl(@Assisted("vertex1") Vertex vertex1, @Assisted("vertex2") Vertex vertex2, @Assisted("vertex3") Vertex vertex3) {
    this.vertices[0] = vertex1;
    this.vertices[1] = vertex2;
    this.vertices[2] = vertex3;
  }

  /**
   * {@inheritDoc}
   */
  public Vertex[] getVertices() {
    return Arrays.copyOf(vertices, vertices.length);
  }

  /**
   * {@inheritDoc}
   */
  public VertexTriangle render(MatrixStack matrixStack, VertexBuffer vertexBuffer) {
    matrixStack
        .push()
        .scale(1, -1, -1);
    for (Vertex vertex : this.vertices) {
      vertex.render(matrixStack, vertexBuffer);
    }
    matrixStack.pop();
    return this;
  }

}
