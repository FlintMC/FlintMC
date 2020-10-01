package net.labyfy.component.render.vbo.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.vbo.VertexAttribute;
import net.labyfy.component.render.vbo.VertexFormat;

import java.util.List;

import static org.lwjgl.opengl.GL33.*;

@Implement(VertexFormat.class)
public class DefaultVertexFormat implements VertexFormat {

  private final List<VertexAttribute> attributes;
  private final int id;
  private final int stride;

  private int oldVao;

  @AssistedInject
  private DefaultVertexFormat(@Assisted List<VertexAttribute> attributes, @Assisted int id) {
    this.attributes = attributes;
    this.id = id;
    this.stride =
        this.attributes.stream().mapToInt(VertexAttribute::getSize).reduce(0, Integer::sum);
    this.oldVao = 0;
  }

  @Override
  public int getAttributeCount() {
    return attributes.size();
  }

  @Override
  public int getVertexSize() {
    return this.stride;
  }

  @Override
  public List<VertexAttribute> getAttributes() {
    return this.attributes;
  }

  @Override
  public int getID() {
    return this.id;
  }

  @Override
  public void bind() {
    this.oldVao = glGetInteger(GL_VERTEX_ARRAY_BINDING);
    glBindVertexArray(this.id);
  }

  @Override
  public void unbind() {
    glBindVertexArray(this.oldVao);
  }
}
