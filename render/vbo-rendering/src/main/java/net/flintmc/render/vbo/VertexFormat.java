package net.flintmc.render.vbo;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.util.List;

/** Represents a vertex attribute format. */
public interface VertexFormat {

  /** @return the number of generic vertex attribute per vertex this vertex format defines. */
  int getAttributeCount();

  /**
   * @return the size of a vertex in floats (same as the sum over the sizes of all generic vertex
   *     attributes contained in this format).
   */
  int getVertexSize();

  /**
   * @return a list of all generic {@link VertexAttribute}s currently contained in this format. The
   *     list shall not be modified.
   */
  List<VertexAttribute> getAttributes();

  /**
   * Creates a new OpenGL vertex array object based on this format.
   *
   * @return the OpenGL name of the newly creates vertex array object.
   */
  int createVAO();

  /**
   * Uploads this format to a given OpenGL vertex array object.
   *
   * @param vao the OpenGL name of the vertex array object this format should be uploaded to.
   */
  void pushToGPU(int vao);

  @AssistedFactory(VertexFormat.class)
  interface Factory {

    /**
     * Creates a new {@link VertexFormat}.
     *
     * @param attributes the generic vertex attributes that will make up a vertex according to the
     *     format.
     * @return the new {@link VertexFormat}.
     */
    VertexFormat create(@Assisted List<VertexAttribute> attributes);
  }
}
