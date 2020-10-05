package net.labyfy.component.render.vbo.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.vbo.VboDrawMode;
import net.labyfy.component.render.vbo.VertexIndexObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

/** {@inheritDoc} */
@Implement(VertexIndexObject.class)
public class DefaultVertexIndexObject implements VertexIndexObject {

  private final List<Integer> indices;
  private final int id;
  private final VboDrawMode drawMode;

  private boolean isAvailable;
  private int oldEbo;

  @AssistedInject
  private DefaultVertexIndexObject() {
    this(VboDrawMode.TRIANGLES);
  }

  @AssistedInject
  private DefaultVertexIndexObject(@Assisted VboDrawMode drawMode) {
    this.indices = new ArrayList<>();
    this.id = glGenBuffers();
    this.isAvailable = false;
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
}
