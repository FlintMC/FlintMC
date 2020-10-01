package net.labyfy.component.render.vbo;

import net.labyfy.component.inject.assisted.AssistedFactory;

public interface VertexIndexObject {

  void addIndices(int... indices);

  void pushToGPU();

  int getID();

  void bind();

  void unbind();

  void draw();

  void drawWithoutBind();

  boolean isAvailable();

  @AssistedFactory(VertexIndexObject.class)
  interface Factory {

    VertexIndexObject create(VertexBufferObject vertexBufferObject);

    VertexIndexObject create(VertexBufferObject vertexBufferObject, VboDrawMode drawMode);
  }
}
