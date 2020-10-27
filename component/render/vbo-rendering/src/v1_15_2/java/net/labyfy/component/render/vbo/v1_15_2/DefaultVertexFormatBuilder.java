package net.labyfy.component.render.vbo.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.vbo.VertexAttribute;
import net.labyfy.component.render.vbo.VertexFormat;
import net.labyfy.component.render.vbo.VertexFormatBuilder;

import java.util.ArrayList;
import java.util.List;

/** {@inheritDoc} */
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

  /** {@inheritDoc} */
  @Override
  public VertexFormatBuilder addAttribute(VertexAttribute attribute) {
    this.currentAttributes.add(attribute);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public VertexFormat build() {
    VertexFormat format = this.vertexFormatFactory.create(this.currentAttributes);
    this.currentAttributes = new ArrayList<>();
    return format;
  }
}
