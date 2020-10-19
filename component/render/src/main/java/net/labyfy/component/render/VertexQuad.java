package net.labyfy.component.render;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.awt.*;

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
   * @param vertexBuffer the vertex data to render into
   * @return this
   */
  VertexQuad render(VertexBuffer vertexBuffer);

  VertexQuad setLightmapUV(int lightmapUV);

  VertexQuad setColor(Color color);

  @AssistedFactory(VertexQuad.class)
  interface Factory {
    VertexQuad create(@Assisted("vertex1") Vertex vertex1,
                      @Assisted("vertex2") Vertex vertex2,
                      @Assisted("vertex3") Vertex vertex3,
                      @Assisted("vertex4") Vertex vertex4);
  }

}
