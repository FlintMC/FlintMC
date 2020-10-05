package net.labyfy.component.render.vbo;

import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.List;

/**
 * Represents an OpenGL buffer object containing indices of vertices contained in a vertex buffer
 * object.
 */
public interface VertexIndexObject {

  /**
   * Adds indices to the buffer.
   *
   * @param indices the indices to be added.
   */
  void addIndices(int... indices);

  /**
   * @return a list of indices currently contained in this buffer. The list should not be modified.
   */
  List<Integer> getIndices();

  /** @return the number of indices currently added. */
  int getSize();

  /** Pushes the current list of indices to the GPU. Should usually not be called manually. */
  void pushToGPU();

  /** @return the OpenGL name of the buffer object. */
  int getID();

  /** Binds this buffer object with OpenGL. */
  void bind();

  /** Unbinds this buffer object and binds the buffer object that was previously bound (if any). */
  void unbind();

  /** @return true, if this buffer object was pushed with OpenGL. */
  boolean isAvailable();

  /** @return the {@link VboDrawMode} this index buffer expects to be drawn with. */
  VboDrawMode getDrawMode();

  @AssistedFactory(VertexIndexObject.class)
  interface Factory {

    /**
     * Creates a new {@link VertexIndexObject} with TRIANGLES as draw mode.
     *
     * @return the new {@link VertexIndexObject}.
     */
    VertexIndexObject create();

    /**
     * Creates a new {@link VertexIndexObject}.
     *
     * @param drawMode the draw mode this index buffer expects to be drawn with.
     * @return the new {@link VertexIndexObject}.
     */
    VertexIndexObject create(VboDrawMode drawMode);
  }
}
