package net.flintmc.render.vbo.v1_16_4;

import static org.lwjgl.opengl.GL33.GL_FLOAT;
import static org.lwjgl.opengl.GL33.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL33.glGenVertexArrays;
import static org.lwjgl.opengl.GL33.glVertexAttribPointer;

import java.util.Collections;
import java.util.List;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.vbo.VertexAttribute;
import net.flintmc.render.vbo.VertexFormat;

/** {@inheritDoc} */
@Implement(VertexFormat.class)
public class VersionedVertexFormat implements VertexFormat {

  private final List<VertexAttribute> attributes;
  private final int stride;

  @AssistedInject
  private VersionedVertexFormat(@Assisted List<VertexAttribute> attributes) {
    this.attributes = attributes;
    this.stride =
        this.attributes.stream().mapToInt(VertexAttribute::getSize).reduce(0, Integer::sum);
  }

  /** {@inheritDoc} */
  @Override
  public int getAttributeCount() {
    return attributes.size();
  }

  /** {@inheritDoc} */
  @Override
  public int getVertexSize() {
    return this.stride;
  }

  /** {@inheritDoc} */
  @Override
  public List<VertexAttribute> getAttributes() {
    return Collections.unmodifiableList(this.attributes);
  }

  /** {@inheritDoc} */
  @Override
  public int createVAO() {
    return glGenVertexArrays();
  }

  /** {@inheritDoc} */
  @Override
  public void pushToGPU(int vao) {
    int index = 0;
    int offset = 0;

    for (VertexAttribute attribute : this.attributes) {
      glVertexAttribPointer(
          index, attribute.getSize(), GL_FLOAT, false, this.getVertexSize() * Float.BYTES, offset);
      glEnableVertexAttribArray(index);
      index++;
      offset += attribute.getSize() * Float.BYTES;
    }
  }
}
