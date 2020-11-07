package net.flintmc.render.vbo.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.vbo.VboDrawMode;
import net.flintmc.render.vbo.VertexIndexObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

/** {@inheritDoc} */
@Implement(VertexIndexObject.class)
public class VersionedVertexIndexObject implements VertexIndexObject {

  private final List<Integer> indices;
  private final int id;
  private final VboDrawMode drawMode;

  private boolean isAvailable;
  private boolean deleted;
  private int oldEbo;

  @AssistedInject
  private VersionedVertexIndexObject() {
    this(VboDrawMode.TRIANGLES);
  }

  @AssistedInject
  private VersionedVertexIndexObject(@Assisted VboDrawMode drawMode) {
    this.indices = new ArrayList<>();
    this.id = glGenBuffers();
    this.isAvailable = false;
    this.deleted = false;
    this.oldEbo = 0;
    this.drawMode = drawMode;
  }

  /** {@inheritDoc} */
  @Override
  public void addIndices(int... indices) {
    if (this.isAvailable)
      throw new IllegalStateException(
          "This EBO has already been pushed to the GPU, indices can't be added anymore.");
    for (int index : indices) {
      this.indices.add(index);
    }
  }

  /** {@inheritDoc} */
  @Override
  public List<Integer> getIndices() {
    return Collections.unmodifiableList(this.indices);
  }

  /** {@inheritDoc} */
  @Override
  public int getSize() {
    return this.indices.size();
  }

  /** {@inheritDoc} */
  @Override
  public void pushToGPU() {
    if (this.isAvailable)
      throw new IllegalStateException("This EBO has already been pushed to the GPU.");

    int[] indicesArray = new int[indices.size()];
    for (int i = 0; i < this.indices.size(); i++) {
      indicesArray[i] = this.indices.get(i);
    }

    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesArray, GL_STATIC_DRAW);

    this.isAvailable = true;
  }

  /** {@inheritDoc} */
  @Override
  public int getID() {
    return this.id;
  }

  /** {@inheritDoc} */
  @Override
  public void bind() {
    this.oldEbo = glGetInteger(GL_ELEMENT_ARRAY_BUFFER_BINDING);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.id);
  }

  /** {@inheritDoc} */
  @Override
  public void unbind() {
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, Math.max(this.oldEbo, 0));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAvailable() {
    return this.isAvailable;
  }

  /** {@inheritDoc} */
  @Override
  public VboDrawMode getDrawMode() {
    return this.drawMode;
  }

  /** {@inheritDoc} */
  @Override
  public void delete() {
    if (this.deleted) throw new IllegalStateException("The EBO was already deleted.");
    this.bind();
    glDeleteBuffers(this.id);
    this.unbind();
    this.deleted = true;
  }
}
