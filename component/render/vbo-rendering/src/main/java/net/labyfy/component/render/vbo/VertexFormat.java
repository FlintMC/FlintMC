package net.labyfy.component.render.vbo;

public interface VertexFormat {

  int getAttributeCount();

  int getVertexSize();

  int getID();

  void bind();

  void unbind();
}
