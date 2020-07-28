package net.labyfy.component.render;

import java.nio.ByteBuffer;

public interface VertexFormat {

  VertexFormat pushFloats(ByteBuffer byteBuffer, VertexBuffer bufferBuilder, String name, float... floats);

  VertexFormat pushBytes(ByteBuffer byteBuffer, VertexBuffer bufferBuilder, String name, byte... bytes);

  VertexFormatElement[] getElements();

  boolean hasElement(String name);

  VertexFormatElement getElement(String name);

  int getBytes();

  int getByteOffset(String name);

  <T> T getHandle();
}
