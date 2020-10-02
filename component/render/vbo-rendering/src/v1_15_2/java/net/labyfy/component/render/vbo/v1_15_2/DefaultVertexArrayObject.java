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

  private final int id;
  private int oldId;

  @AssistedInject
  private DefaultVertexArrayObject(@Assisted VertexBufferObject vbo) {
    this(vbo, () -> {});
  }

  @AssistedInject
  private DefaultVertexArrayObject(
      @Assisted VertexBufferObject vbo, @Assisted Runnable bindCallback) {
    this.format = vbo.getFormat();
    this.vbo = vbo;

    this.id = this.format.createVAO();

    this.bind();

    this.vbo.bind();
    this.vbo.pushToGPU();

    this.format.pushToGPU(this.id);

    bindCallback.run();

    this.vbo.unbind();
    this.unbind();
  }

  @Override
  public void draw(VertexIndexObject ebo) {
    this.bind();
    this.vbo.bind();
    ebo.bind();
    this.drawWithoutBind(ebo);
    this.unbind();
    this.vbo.unbind();
    ebo.unbind();
  }

  @Override
  public void drawWithoutBind(VertexIndexObject ebo) {
    if (ebo.getDrawMode() == VboDrawMode.TRIANGLES)
      glDrawElements(GL_TRIANGLES, ebo.getSize(), GL_UNSIGNED_INT, 0);
    else if (ebo.getDrawMode() == VboDrawMode.QUADS)
      glDrawElements(GL_QUADS, ebo.getSize(), GL_UNSIGNED_INT, 0);
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
