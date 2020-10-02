package net.labyfy.component.render.vbo;

import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.List;

public interface VertexFormat {

  int getAttributeCount();

  int getVertexSize();

  List<VertexAttribute> getAttributes();

  int getID();

  void bind();

  void unbind();

  @AssistedFactory(VertexFormat.class)
  interface Factory {

    VertexFormat create(List<VertexAttribute> attributes, int id);
  }
}
