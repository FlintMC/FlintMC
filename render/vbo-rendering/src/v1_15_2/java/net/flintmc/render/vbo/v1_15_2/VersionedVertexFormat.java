package net.flintmc.render.vbo.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.vbo.VertexAttribute;
import net.flintmc.render.vbo.VertexFormat;

import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

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
