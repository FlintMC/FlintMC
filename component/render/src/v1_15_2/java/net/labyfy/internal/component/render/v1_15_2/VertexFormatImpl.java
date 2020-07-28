package net.labyfy.internal.component.render.v1_15_2;

import com.google.common.collect.ImmutableList;
import net.labyfy.component.render.VertexBuffer;
import net.labyfy.component.render.VertexFormat;
import net.labyfy.component.render.VertexFormatElement;

import java.nio.ByteBuffer;

public class VertexFormatImpl implements VertexFormat {

  private final VertexFormatElementImpl[] elements;
  private final net.minecraft.client.renderer.vertex.VertexFormat handle;

  public VertexFormatImpl(VertexFormatElementImpl[] elements) {
    this.elements = elements;
    this.handle = this.createHandle();
  }

  private net.minecraft.client.renderer.vertex.VertexFormat createHandle() {
    ImmutableList.Builder<net.minecraft.client.renderer.vertex.VertexFormatElement> elements = ImmutableList.<net.minecraft.client.renderer.vertex.VertexFormatElement>builder();
    for (VertexFormatElementImpl element : this.elements) {
      elements.add(element.<net.minecraft.client.renderer.vertex.VertexFormatElement>getHandle());
    }
    return new net.minecraft.client.renderer.vertex.VertexFormat(elements.build());

  }

  public VertexFormatImpl pushFloats(ByteBuffer byteBuffer, VertexBuffer vertexBuffer, String name, float... floats) {
    int offset = (vertexBuffer.getVertexCount() * vertexBuffer.getFormat().getBytes()) + vertexBuffer.getFormat().getByteOffset(name);
    byteBuffer.position(offset);
    for (float f : floats) {
      byteBuffer.putFloat(f);
    }
    return this;
  }

  public VertexFormatImpl pushBytes(ByteBuffer byteBuffer, VertexBuffer vertexBuffer, String name, byte... bytes) {
    int offset = (vertexBuffer.getVertexCount() * vertexBuffer.getFormat().getBytes()) + vertexBuffer.getFormat().getByteOffset(name);
    byteBuffer.position(offset);
    byteBuffer.put(bytes);
    return this;
  }

  public VertexFormatImpl pushBytes(ByteBuffer byteBuffer, VertexBuffer vertexBuffer, String name, ByteBuffer bytes) {
    int offset = (vertexBuffer.getVertexCount() * vertexBuffer.getFormat().getBytes()) + vertexBuffer.getFormat().getByteOffset(name);
    byteBuffer.position(offset);
    byteBuffer.put(bytes);
    return this;
  }

  public VertexFormatElementImpl[] getElements() {
    return this.elements;
  }

  public boolean hasElement(String name) {
    for (VertexFormatElementImpl element : this.elements) {
      if (element.getName().equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }

  public VertexFormatElement getElement(String name) {
    for (VertexFormatElementImpl element : this.elements) {
      if (element.getName().equalsIgnoreCase(name)) {
        return element;
      }
    }
    return null;
  }

  public int getBytes() {
    int bytes = 0;
    for (VertexFormatElementImpl element : this.elements) {
      bytes += element.getAmount() * element.getType().getSize();
    }
    return bytes;
  }

  public int getByteOffset(String name) {
    int offset = 0;
    for (VertexFormatElementImpl element : this.elements) {
      if (name.equalsIgnoreCase(element.getName())) {
        return offset;
      }
      offset += element.getAmount() * element.getType().getSize();
    }
    throw new IllegalStateException();
  }

  public <T> T getHandle() {
    return (T) this.handle;
  }
}