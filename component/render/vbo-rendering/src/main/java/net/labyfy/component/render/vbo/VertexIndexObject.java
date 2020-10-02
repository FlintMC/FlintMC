package net.labyfy.component.render.vbo;

import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.List;

public interface VertexIndexObject {

  void addIndices(int... indices);

  List<Integer> getIndices();

  int getSize();

  void pushToGPU();

  int getID();

  void bind();

  void unbind();

  boolean isAvailable();

  VboDrawMode getDrawMode();

  @AssistedFactory(VertexIndexObject.class)
  interface Factory {

    VertexIndexObject create();

    VertexIndexObject create(VboDrawMode drawMode);
  }
}
