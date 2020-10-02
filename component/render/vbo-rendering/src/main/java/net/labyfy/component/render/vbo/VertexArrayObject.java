package net.labyfy.component.render.vbo;

import net.labyfy.component.inject.assisted.AssistedFactory;

public interface VertexArrayObject {

  void draw(VertexIndexObject ebo);

  void drawWithoutBind(VertexIndexObject ebo);

  VertexFormat getFormat();

  VertexBufferObject getVBO();

  void bind();

  void unbind();

  int getID();

  @AssistedFactory(VertexArrayObject.class)
  interface Factory {

    VertexArrayObject create(VertexBufferObject vbo);

    VertexArrayObject create(VertexBufferObject vbo, Runnable bindCallback);

  }
}
