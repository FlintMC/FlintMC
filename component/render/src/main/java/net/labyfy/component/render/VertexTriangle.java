package net.labyfy.component.render;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

public interface VertexTriangle {

  /**
   * @return all vertices used to build this triangle
   */
  Vertex[] getVertices();

  /**
   * Renders this triangle to a given 3d context
   *
   * @param matrixStack  the world context to render into
   * @param vertexBuffer the vertex data to render into
   * @return this
   */
  VertexTriangle render(MatrixStack matrixStack, VertexBuffer vertexBuffer);

  @AssistedFactory(VertexTriangle.class)
  interface Factory {
    VertexTriangle create(@Assisted("vertex1") Vertex vertex1, @Assisted("vertex2") Vertex vertex2, @Assisted("vertex3") Vertex vertex3);
  }
}
