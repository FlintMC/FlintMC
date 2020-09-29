package net.labyfy.internal.component.render.v1_15_2;

import java.nio.ByteBuffer;

public interface BufferBuilderAccessor {
  ByteBuffer getByteBuffer();

  void setByteBuffer(ByteBuffer byteBuffer);

  void setVertexCount(int vertexCount);
}
