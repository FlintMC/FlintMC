package net.labyfy.component.render.vbo;

public enum VertexAttributes implements VertexAttribute {
  POSITION3F(3),
  POSITION4F(4),
  NORMAL(3),
  COLOR_RGB(3),
  COLOR_RGBA(4),
  TEXTURE_UV(2);

  private final int size;

  VertexAttributes(int size) {
    this.size = size;
  }

  @Override
  public int getSize() {
    return this.size;
  }
}
