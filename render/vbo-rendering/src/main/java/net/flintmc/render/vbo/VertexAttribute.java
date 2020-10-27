package net.flintmc.render.vbo;

/** Represents a generic vertex attribute that can be part of a vertex format. */
public interface VertexAttribute {

  /** @return the size of this {@link VertexAttribute} in floats. */
  int getSize();
}
