package net.labyfy.component.render;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

/**
 * Represents a quad in the 3 dimensional coordinate space.
 * Utility class to render quads more easily and match the minecraft structure better.
 */
public interface VertexQuad {

  /**
   * @return all vertices used to build this quad
   */
  Vertex[] getVertices();

  /**
   * Renders this quad to a given 3d context
   *
   * @param matrixStack  the world context to render into
   * @param vertexBuffer the vertex data to render into
   * @return this
   */
  VertexQuad render(MatrixStack matrixStack, VertexBuffer vertexBuffer);

  @AssistedFactory(VertexQuad.class)
  interface Factory {
    VertexQuad create(@Assisted("vertex1") Vertex vertex1,
                      @Assisted("vertex2") Vertex vertex2,
                      @Assisted("vertex3") Vertex vertex3,
                      @Assisted("vertex4") Vertex vertex4);
  }

}
