package net.flintmc.render.vbo;

/** Lists some predefined generic vertex attributes. */
public enum VertexAttributes implements VertexAttribute {
  /** Represents a generic 3D position. */
  POSITION3F(3),
  /** Represents a homogenous 3D position. */
  POSITION4F(4),
  /** Represents a 3D normal vector. */
  NORMAL(3),
  /** Represents a RGB color. */
  COLOR_RGB(3),
  /** Represents a RGBA color. */
  COLOR_RGBA(4),
  /** Represents a texture coordinate. */
  TEXTURE_UV(2);

  private final int size;

  VertexAttributes(int size) {
    this.size = size;
  }

  /** {@inheritDoc} */
  @Override
  public int getSize() {
    return this.size;
  }
}
