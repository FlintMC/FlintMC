package net.labyfy.component.render.vbo;

import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.List;

public interface VertexFormat {

  int getAttributeCount();

  int getVertexSize();

  List<VertexAttribute> getAttributes();

  int createVAO();

  void pushToGPU(int vao);

  @AssistedFactory(VertexFormat.class)
  interface Factory {

    VertexFormat create(List<VertexAttribute> attributes);
  }
}
