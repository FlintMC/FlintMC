package net.labyfy.component.render.vbo.v1_15_2;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.vbo.VertexAttribute;
import net.labyfy.component.render.vbo.VertexFormat;
import net.labyfy.component.render.vbo.VertexFormatBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

@Singleton
@Implement(VertexFormatBuilder.class)
public class DefaultVertexFormatBuilder implements VertexFormatBuilder {

  private List<VertexAttribute> currentAttributes;
  private final VertexFormat.Factory vertexFormatFactory;

  @Inject
  private DefaultVertexFormatBuilder(VertexFormat.Factory vertexFormatFactory) {
    this.currentAttributes = new ArrayList<>();
    this.vertexFormatFactory = vertexFormatFactory;
  }

  @Override
  public VertexFormatBuilder addAttribute(VertexAttribute attribute) {
    this.currentAttributes.add(attribute);
    return this;
  }

  @Override
  public VertexFormat build() {

    int id = glGenVertexArrays();
    VertexFormat format = this.vertexFormatFactory.create(this.currentAttributes, id);

    int index = 0;
    int offset = 0;

    format.bind();

    for (VertexAttribute attribute : this.currentAttributes) {
      glVertexAttribPointer(
          index, attribute.getSize(), GL_FLOAT, false, format.getVertexSize() * 4, offset);
      glEnableVertexAttribArray(index);
      index++;
      offset += attribute.getSize() * 4;
    }

    format.unbind();

    this.currentAttributes = new ArrayList<>();
    return format;
  }
}
