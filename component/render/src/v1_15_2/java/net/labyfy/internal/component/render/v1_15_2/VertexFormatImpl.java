package net.labyfy.internal.component.render.v1_15_2;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexFormat;
import net.labyfy.component.render.VertexFormatElement;
import net.labyfy.component.render.VertexFormatElementType;

import java.nio.ByteBuffer;
import java.util.EnumMap;

@Implement(VertexFormat.class)
public class VertexFormatImpl implements VertexFormat {
  private final VertexFormatElement[] elements;
  private final net.minecraft.client.renderer.vertex.VertexFormat handle;
  private final int size;
  private final EnumMap<VertexFormatElementType, Integer> elementOffsets = new EnumMap<>(VertexFormatElementType.class);
  private final EnumMap<VertexFormatElementType, VertexFormatElement> elementsByType = new EnumMap<>(VertexFormatElementType.class);

  @Inject
  private VertexFormatImpl(@Assisted VertexFormatElement[] elements) {
    this.elements = elements;
    this.handle = this.createHandle();

    int offset = 0;
    for (VertexFormatElement element : elements) {
      elementOffsets.put(element.getId(), offset);
      elementsByType.put(element.getId(), element);
      offset += element.getAmount() * element.getType().getSize();
    }

    this.size = offset;
  }

  private net.minecraft.client.renderer.vertex.VertexFormat createHandle() {
    ImmutableList.Builder<net.minecraft.client.renderer.vertex.VertexFormatElement> elements = ImmutableList.<net.minecraft.client.renderer.vertex.VertexFormatElement>builder();
    for (VertexFormatElement element : this.elements) {
      elements.add(element.<net.minecraft.client.renderer.vertex.VertexFormatElement>getHandle());
    }
    return new net.minecraft.client.renderer.vertex.VertexFormat(elements.build());
  }

  /**
   * {@inheritDoc}
   */
  public VertexFormatImpl pushFloats(ByteBuffer byteBuffer, VertexBuffer vertexBuffer, VertexFormatElementType vertexFormatElementType, float... floats) {
    int offset = (vertexBuffer.getVertexCount() * vertexBuffer.getRenderType().getFormat().getSize()) + vertexBuffer.getRenderType().getFormat().getByteOffset(vertexFormatElementType);
    byteBuffer.position(offset);
    for (float f : floats) {
      byteBuffer.putFloat(f);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexFormatImpl pushBytes(ByteBuffer byteBuffer, VertexBuffer vertexBuffer, VertexFormatElementType vertexFormatElementType, byte... bytes) {
    int offset = (vertexBuffer.getVertexCount() * vertexBuffer.getRenderType().getFormat().getSize()) + vertexBuffer.getRenderType().getFormat().getByteOffset(vertexFormatElementType);
    byteBuffer.position(offset);
    byteBuffer.put(bytes);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexFormat pushShorts(ByteBuffer byteBuffer, VertexBuffer vertexBuffer, VertexFormatElementType vertexFormatElementType, short... shorts) {
    int offset = (vertexBuffer.getVertexCount() * vertexBuffer.getRenderType().getFormat().getSize()) + vertexBuffer.getRenderType().getFormat().getByteOffset(vertexFormatElementType);
    byteBuffer.position(offset);
    for (short aShort : shorts) {
      byteBuffer.putShort(aShort);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexFormatImpl pushBytes(ByteBuffer byteBuffer, VertexBuffer vertexBuffer, VertexFormatElementType vertexFormatElementType, ByteBuffer bytes) {
    int offset = (vertexBuffer.getVertexCount() * vertexBuffer.getRenderType().getFormat().getSize()) + vertexBuffer.getRenderType().getFormat().getByteOffset(vertexFormatElementType);
    byteBuffer.position(offset);
    byteBuffer.put(bytes);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public VertexFormatElement[] getElements() {
    return this.elements;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasElement(VertexFormatElementType vertexFormatElementType) {
    return elementOffsets.containsKey(vertexFormatElementType);
  }

  /**
   * {@inheritDoc}
   */
  public VertexFormatElement getElement(VertexFormatElementType vertexFormatElementType) {
    return elementsByType.get(vertexFormatElementType);
  }

  /**
   * {@inheritDoc}
   */
  public int getSize() {
    return this.size;
  }

  /**
   * {@inheritDoc}
   */
  public int getByteOffset(VertexFormatElementType vertexFormatElementType) {
    return this.elementOffsets.get(vertexFormatElementType);
  }

  public <T> T getHandle() {
    return (T) this.handle;
  }
}