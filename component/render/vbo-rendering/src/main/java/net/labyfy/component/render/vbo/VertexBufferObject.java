package net.labyfy.component.render.vbo;

import net.labyfy.component.inject.assisted.AssistedFactory;

/** Represents an OpenGL vertex buffer object (VBO). */
public interface VertexBufferObject {

  /**
   * Creates a new {@link VertexBuilder} and adds it to the list of vertices.
   *
   * @return a new {@link VertexBuilder} that should be used to construct the vertex.
   */
  VertexBuilder addVertex();

  /**
   * Adds a {@link VertexBuilder} to the list of vertices.
   *
   * @param vertexBuilder the {@link VertexBuilder} to be added.
   */
  void addVertex(VertexBuilder vertexBuilder);

  /** @return the number of vertices contained in this vertex buffer object. */
  int getVertexCount();

  /**
   * Pushes the vertices to the OpenGL vertex buffer object. Should usually not be called manually.
   */
  void pushToGPU();

  /** @return the OpenGL name of this vertex buffer object. */
  int getID();

  /** Binds this vertex buffer object with OpenGL. */
  void bind();

  /**
   * Unbinds this vertex buffer object and rebinds the vertex buffer object that was previously
   * bound (if any).
   */
  void unbind();

  /**
   * @return true, if the vertices of this vertex buffer object have been pushed via OpenGL and the
   *     VBO is therefore ready to be used in a draw call.
   */
  boolean isAvailable();

  /**
   * @return the {@link VertexFormat} the vertices of this vertex buffer object should comply with.
   */
  VertexFormat getFormat();

  @AssistedFactory(VertexBufferObject.class)
  interface Factory {

    /**
     * Creates a new {@link VertexBufferObject}.
     *
     * @param vertexFormat the {@link VertexFormat} the vertices should be in.
     * @return the new {@link VertexBufferObject}.
     */
    VertexBufferObject create(VertexFormat vertexFormat);
  }
}
