package net.labyfy.component.render.vbo;

import com.sun.prism.impl.VertexBuffer;
import net.labyfy.component.inject.assisted.AssistedFactory;

public interface VertexArrayObject {

  void draw();

  void drawWithoutBind();

  VertexFormat getFormat();

  VertexBufferObject getVBO();

  VertexIndexObject getEBO();

  void bind();

  void unbind();

  int getID();

  @AssistedFactory(VertexArrayObject.class)
  interface Factory {

    VertexArrayObject create(VertexBufferObject vbo, VertexIndexObject ebo);

    VertexArrayObject create(VertexBufferObject vbo, VertexIndexObject ebo, VboDrawMode drawMode, Runnable bindCallback);
  }
}
