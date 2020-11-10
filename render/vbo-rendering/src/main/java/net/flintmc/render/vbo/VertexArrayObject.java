package net.flintmc.render.vbo;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.nio.IntBuffer;

/**
 * Represents a Vertex Array Object. The VAO contains information about the Vertex Format as well as
 * the Vertex Buffer Object containing the actual vertices to be drawn.
 */
public interface VertexArrayObject {

  /**
   * Draws vertices of the Vertex Buffer Object associated with this VAO. The VAO will be bound and
   * unbound automatically.
   *
   * @param ebo the Vertex Index Buffer that contains the indices of the vertices to draw.
   */
  void draw(VertexIndexObject ebo);

  /**
   * Draws vertices of the associated Vertex Buffer Object. The indices are provided client-side and
   * not via a designated index buffer.
   *
   * @param indices the indices of the vertices in the associated VBO to draw.
   * @param drawMode the OpenGL draw mode to be used to draw the vertices.
   */
  void draw(IntBuffer indices, VboDrawMode drawMode);

  /**
   * Draws vertices of the Vertex Buffer Object associated with this VAO. The VAO will not be bound
   * or unbound automatically.
   *
   * @param ebo the Vertex Index Buffer that contains the indices of the vertices to draw.
   */
  void drawWithoutBind(VertexIndexObject ebo);

  /**
   * @return the {@link VertexFormat} that describes the format of the vertices in the Vertex Buffer
   *     Object associated with this VAO.
   */
  VertexFormat getFormat();

  /** @return the {@link VertexBufferObject} this VAO is associated with. */
  VertexBufferObject getVBO();

  /**
   * Binds this VAO with OpenGL. A previously bound VAO will be unbound and rebound when calling
   * unbind.
   */
  void bind();

  /** Unbinds this VAO. A previously bound VAO will be rebound automatically. */
  void unbind();

  /** @return the OpenGL name of this VAO. */
  int getID();

  /** Deletes the vertex array object and the corresponding vertex buffer object. */
  void delete();

  @AssistedFactory(VertexArrayObject.class)
  interface Factory {

    /**
     * Creates a new {@link VertexArrayObject}.
     *
     * @param vbo the {@link VertexBufferObject} the new VAO should be associated with. New vertices
     *     can't be added to the VBO after the VAO is created.
     * @return the new VAO.
     */
    VertexArrayObject create(@Assisted VertexBufferObject vbo);

    /**
     * Creates a new {@link VertexArrayObject}.
     *
     * @param vbo the {@link VertexBufferObject} the new VAO should be associated with. New vertices
     *     can't be added to the VBO after the VAO is created.
     * @param bindCallback a callback that will be executed after this VAO and the associated VBO
     *     will be bound and pushed to the GPU but before they get unbound. The callback can be used
     *     to further configure or change the VAO.
     * @return the new VAO.
     */
    VertexArrayObject create(@Assisted VertexBufferObject vbo, @Assisted Runnable bindCallback);
  }
}
