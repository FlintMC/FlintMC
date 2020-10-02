package net.labyfy.component.render.vbo.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.vbo.VertexBufferObject;
import net.labyfy.component.render.vbo.VertexBuilder;
import net.labyfy.component.render.vbo.VertexFormat;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

@Implement(VertexBufferObject.class)
public class DefaultVertexBufferObject implements VertexBufferObject {

  private final VertexFormat vertexFormat;
  private final VertexBuilder.Factory vertexBuilderFactory;
  private final int id;

  private List<VertexBuilder> vertices;
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
    VertexBuilder builder = this.vertexBuilderFactory.create(this);
    this.vertices.add(builder);
    return builder;
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
    float[] buffer = new float[totalSize];
    int offset = 0;
    for (VertexBuilder vertex : this.vertices) {
      offset += vertex.write(buffer, offset);
    }

    this.previousVbo = glGetInteger(GL_ARRAY_BUFFER_BINDING);


    this.bind();
    glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

    this.unbind();

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
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ARRAY_BUFFER, this.id);
  }

  @Override
  public void unbind() {
    glBindBuffer(GL_ARRAY_BUFFER, Math.max(this.previousVbo, 0));
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
