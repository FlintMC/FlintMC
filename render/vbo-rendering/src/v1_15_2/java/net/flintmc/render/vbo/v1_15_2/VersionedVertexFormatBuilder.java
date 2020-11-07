package net.flintmc.render.vbo.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.vbo.VertexAttribute;
import net.flintmc.render.vbo.VertexFormat;
import net.flintmc.render.vbo.VertexFormatBuilder;

import java.util.ArrayList;
import java.util.List;

/** {@inheritDoc} */
@Singleton
@Implement(VertexFormatBuilder.class)
public class VersionedVertexFormatBuilder implements VertexFormatBuilder {

  private final VertexFormat.Factory vertexFormatFactory;
  private List<VertexAttribute> currentAttributes;

  @Inject
  private VersionedVertexFormatBuilder(VertexFormat.Factory vertexFormatFactory) {
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
