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
  private final int stride;

  @AssistedInject
  private DefaultVertexFormat(@Assisted List<VertexAttribute> attributes) {
    this.attributes = attributes;
    this.stride =
        this.attributes.stream().mapToInt(VertexAttribute::getSize).reduce(0, Integer::sum);
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
  public int createVAO() {
    return glGenVertexArrays();
  }

  @Override
  public void pushToGPU(int vao) {
    int index = 0;
    int offset = 0;

    for (VertexAttribute attribute : this.attributes) {
      glVertexAttribPointer(
              index, attribute.getSize(), GL_FLOAT, false, this.getVertexSize() * 4, offset);
      glEnableVertexAttribArray(index);
      index++;
      offset += attribute.getSize() * 4;
    }

  }

}
