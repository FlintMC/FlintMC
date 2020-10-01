package net.labyfy.component.render.vbo;

import net.labyfy.component.inject.assisted.AssistedFactory;

public interface VertexBufferObject {

  VertexBuilder addVertex();

  void addVertex(VertexBuilder vertexBuilder);

  void pushToGPU();

  int getID();

  void bind();

  void unbind();

  boolean isAvailable();

  @AssistedFactory(VertexBufferObject.class)
  interface Factory {

    VertexBufferObject create(VertexFormat vertexFormat);
  }
}
