package net.labyfy.component.render.vbo.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.vbo.VboDrawMode;
import net.labyfy.component.render.vbo.VertexBufferObject;
import net.labyfy.component.render.vbo.VertexIndexObject;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

@Implement(VertexIndexObject.class)
public class DefaultVertexIndexObject implements VertexIndexObject {

  private final VertexBufferObject vbo;
  private final VboDrawMode drawMode;

  private final List<Integer> indices;
  private final int id;

  private boolean isAvailable;
  private int oldEbo;

  @AssistedInject
  private DefaultVertexIndexObject(@Assisted VertexBufferObject vbo) {
    this(vbo, VboDrawMode.TRIANGLES);
  }

  @AssistedInject
  private DefaultVertexIndexObject(
      @Assisted VertexBufferObject vbo, @Assisted VboDrawMode drawMode) {
    this.vbo = vbo;
    this.drawMode = drawMode;
    this.indices = new ArrayList<>();
    this.id = glGenBuffers();
    this.isAvailable = false;
    this.oldEbo = 0;
  }

  @Override
  public void addIndices(int... indices) {
    if (this.isAvailable)
      throw new IllegalStateException(
          "This EBO has already been pushed to the GPU, indices can't be added anymore.");
    for (int index : indices) {
      this.indices.add(index);
    }
  }

  @Override
  public void pushToGPU() {
    if (this.isAvailable)
      throw new IllegalStateException("This EBO has already been pushed to the GPU.");

    int[] indicesArray = new int[indices.size()];
    for (int i = 0; i < this.indices.size(); i++) {
      indicesArray[i] = this.indices.get(i);
    }

    this.bind();
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesArray, GL_STATIC_DRAW);
    this.unbind();

    this.isAvailable = true;
  }

  @Override
  public int getID() {
    return this.id;
  }

  @Override
  public void bind() {
    this.oldEbo = glGetInteger(GL_ELEMENT_ARRAY_BUFFER_BINDING);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.id);
  }

  @Override
  public void unbind() {
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, Math.max(this.oldEbo, 0));
  }

  @Override
  public void draw() {
    if (!this.isAvailable)
      throw new IllegalStateException(
          "This EBO has not yet been pushed to the GPU and is therefore not available for drawing.");
    if (!this.vbo.isAvailable())
      throw new IllegalStateException(
          "The VBO this EBO refers to has not yet been pushed to the GPU and is therefore not available for drawing.");

    this.vbo.getFormat().bind();
    this.vbo.bind();
    this.bind();

    this.drawWithoutBind();

    this.vbo.getFormat().unbind();
    this.vbo.unbind();
    this.unbind();
  }

  @Override
  public void drawWithoutBind() {
    if (this.drawMode == VboDrawMode.TRIANGLES)
      glDrawElements(GL_TRIANGLES, this.indices.size(), GL_UNSIGNED_INT, 0);
    if (this.drawMode == VboDrawMode.QUADS)
      glDrawElements(GL_QUADS, this.indices.size(), GL_UNSIGNED_INT, 0);
  }

  @Override
  public boolean isAvailable() {
    return this.isAvailable;
  }
}
