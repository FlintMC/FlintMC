package net.labyfy.component.render;

import java.nio.ByteBuffer;

public interface AdvancedVertexBuffer {

  AdvancedVertexBuffer pushFloats(String name, float... floats);

  AdvancedVertexBuffer pushBytes(String name, byte... bytes);

  AdvancedVertexBuffer pushShorts(String name, short... shorts);

  AdvancedVertexBuffer incrementVertexCount(int count);

  VertexBuffer simple();

  ByteBuffer getByteBuffer();

  AdvancedVertexBuffer setByteBuffer(ByteBuffer byteBuffer);

}
