package net.labyfy.internal.component.render;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.Vertex;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexTriangle;

import java.awt.*;
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
  public VertexTriangle render(VertexBuffer vertexBuffer) {
    for (int i = 0; i < 3; i++)
      this.vertices[i].render(vertexBuffer);
    return this;
  }

  public VertexTriangle setLightmapUV(int lightmapUV) {
    for (int i = 0; i < 3; i++) {
      vertices[i].setLightmapUV(lightmapUV);
    }
    return this;
  }

  public VertexTriangle setColor(Color color) {
    for (int i = 0; i < 3; i++) {
      vertices[i].setColor(color);
    }
    return this;
  }

}
