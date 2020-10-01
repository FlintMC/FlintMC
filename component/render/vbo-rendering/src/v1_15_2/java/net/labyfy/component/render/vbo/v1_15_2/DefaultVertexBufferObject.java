package net.labyfy.component.render.vbo.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.vbo.VertexBufferObject;
import net.labyfy.component.render.vbo.VertexBuilder;
import net.labyfy.component.render.vbo.VertexFormat;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

@Implement(VertexBufferObject.class)
public class DefaultVertexBufferObject implements VertexBufferObject {

  private final VertexFormat vertexFormat;
  private final VertexBuilder.Factory vertexBuilderFactory;
  private final int id;

  private List<VertexBuilder> vertices;
  private FloatBuffer buffer;
  private boolean isAvailable;
  private int previousVbo;

  @AssistedInject
  private DefaultVertexBufferObject(
      @Assisted VertexFormat vertexFormat, VertexBuilder.Factory vertexBuilderFactory) {
    this.vertexFormat = vertexFormat;
    this.vertexBuilderFactory = vertexBuilderFactory;
    this.vertices = new ArrayList<>();
    this.id = glGenBuffers();
    this.isAvailable = false;
  }

  @Override
  public VertexBuilder addVertex() {
    if (isAvailable)
      throw new IllegalStateException(
          "This VBO is already pushed to the GPU, vertices can't be added anymore.");
    return this.vertexBuilderFactory.create(this);
  }

  @Override
  public void addVertex(VertexBuilder vertexBuilder) {
    if (isAvailable)
      throw new IllegalStateException(
          "This VBO is already pushed to the GPU, vertices can't be added anymore.");
    this.vertices.add(vertexBuilder);
  }

  @Override
  public void pushToGPU() {
    if (isAvailable) throw new IllegalStateException("This VBO is already pushed to the GPU.");
    int totalSize = vertices.size() * vertexFormat.getVertexSize();
    this.buffer = MemoryUtil.memAllocFloat(totalSize);
    this.vertices.forEach(vertex -> vertex.write(this.buffer));
    this.buffer.rewind();

    this.previousVbo = glGetInteger(GL_ARRAY_BUFFER_BINDING);

    glBindBuffer(GL_ARRAY_BUFFER, this.id);
    glBufferData(GL_ARRAY_BUFFER, this.buffer, GL_STATIC_DRAW);

    glBindBuffer(GL_ARRAY_BUFFER, this.previousVbo);

    this.isAvailable = true;
    this.vertices = null;
  }

  @Override
  public int getID() {
    return this.id;
  }

  @Override
  public void bind() {
    this.previousVbo = glGetInteger(GL_ARRAY_BUFFER_BINDING);
    glBindBuffer(GL_ARRAY_BUFFER, this.id);
  }

  @Override
  public void unbind() {
    glBindBuffer(GL_ARRAY_BUFFER, this.previousVbo);
  }

  @Override
  public boolean isAvailable() {
    return this.isAvailable;
  }

  @Override
  public VertexFormat getFormat() {
    return this.vertexFormat;
  }
}
