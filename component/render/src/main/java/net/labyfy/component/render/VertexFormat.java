package net.labyfy.component.render;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.nio.ByteBuffer;


public interface VertexFormat {

  VertexFormat pushFloats(ByteBuffer byteBuffer, VertexBuffer bufferBuilder, String name, float... floats);

  VertexFormat pushBytes(ByteBuffer byteBuffer, VertexBuffer bufferBuilder, String name, byte... bytes);

  VertexFormat pushShorts(ByteBuffer byteBuffer, VertexBuffer vertexBuffer, String name, short... shorts);

  VertexFormatElement[] getElements();

  boolean hasElement(String name);

  VertexFormatElement getElement(String name);

  int getBytes();

  int getByteOffset(String name);

  <T> T getHandle();

  @AssistedFactory(VertexFormat.class)
  interface Factory {
    VertexFormat create(@Assisted VertexFormatElement... elements);
  }

}
