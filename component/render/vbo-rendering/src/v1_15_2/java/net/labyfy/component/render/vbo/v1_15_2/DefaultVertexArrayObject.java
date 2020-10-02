package net.labyfy.component.render.vbo.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.vbo.*;

import static org.lwjgl.opengl.GL33.*;

@Implement(VertexArrayObject.class)
public class DefaultVertexArrayObject implements VertexArrayObject {

  private final VertexFormat format;
  private final VertexBufferObject vbo;
  private final VertexIndexObject ebo;
  private final VboDrawMode drawMode;

  private final int id;
  private int oldId;

  @AssistedInject
  private DefaultVertexArrayObject(
      @Assisted VertexBufferObject vbo, @Assisted VertexIndexObject ebo) {
    this(vbo, ebo, VboDrawMode.TRIANGLES, () -> {});
  }

  @AssistedInject
  private DefaultVertexArrayObject(
      @Assisted VertexBufferObject vbo,
      @Assisted VertexIndexObject ebo,
      @Assisted VboDrawMode drawMode,
      @Assisted Runnable bindCallback) {
    this.format = vbo.getFormat();
    this.vbo = vbo;
    this.ebo = ebo;
    this.drawMode = drawMode;

    this.id = this.format.createVAO();

    this.bind();

    this.vbo.bind();
    this.vbo.pushToGPU();

    this.ebo.bind();
    this.ebo.pushToGPU();

    this.format.pushToGPU(this.id);

    bindCallback.run();

    this.ebo.unbind();
    this.vbo.unbind();
    this.unbind();
  }

  @Override
  public void draw() {
    this.bind();
    this.vbo.unbind();
    this.ebo.bind();
    this.drawWithoutBind();
    this.unbind();
    this.vbo.unbind();
    this.ebo.unbind();
  }

  @Override
  public void drawWithoutBind() {
    if (drawMode == VboDrawMode.TRIANGLES)
      glDrawElements(GL_TRIANGLES, this.ebo.getSize(), GL_UNSIGNED_INT, 0);
    else if (drawMode == VboDrawMode.QUADS)
      glDrawElements(GL_QUADS, this.ebo.getSize(), GL_UNSIGNED_INT, 0);
  }

  @Override
  public VertexFormat getFormat() {
    return this.format;
  }

  @Override
  public VertexBufferObject getVBO() {
    return this.vbo;
  }

  @Override
  public VertexIndexObject getEBO() {
    return this.ebo;
  }

  @Override
  public void bind() {
    this.oldId = glGetInteger(GL_VERTEX_ARRAY_BINDING);
    glBindVertexArray(this.id);
  }

  @Override
  public void unbind() {
    glBindVertexArray(this.oldId);
  }

  @Override
  public int getID() {
    return this.id;
  }
}
