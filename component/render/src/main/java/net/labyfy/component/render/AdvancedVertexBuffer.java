package net.labyfy.component.render;

import java.nio.ByteBuffer;

/**
 * This class represents a collection of vertices and how they should be rendered.
 * <p>
 * This is necessary because Minecraft starts to use batched rendering in the 1.15.2 and in order to maintain a cross version
 * rendering module, batched rendering had to be implemented.
 * <p>
 * This class is quite similar to the good old BufferBuilder/IVertexConsumer.
 * <p>
 * See also at {@link MatrixStack}
 */
public interface AdvancedVertexBuffer {

  /**
   * Pushes floats to the current vertex and checks for the existence of the {@link VertexFormatElement} defined by name.
   *
   * @param vertexFormatElementType type of the {@link VertexFormatElement}
   * @param floats                  data to push
   * @return this
   */
  AdvancedVertexBuffer pushFloats(VertexFormatElementType vertexFormatElementType, float... floats);

  /**
   * Pushes bytes to the current vertex and checks for the existence of the {@link VertexFormatElement} defined by name.
   *
   * @param vertexFormatElementType type of the {@link VertexFormatElement}
   * @param bytes                   data to push
   * @return this
   */
  AdvancedVertexBuffer pushBytes(VertexFormatElementType vertexFormatElementType, byte... bytes);

  /**
   * Pushes shorts to the current vertex and checks for the existence of the {@link VertexFormatElement} defined by name.
   *
   * @param vertexFormatElementType type of the {@link VertexFormatElement}
   * @param shorts                  data to push
   * @return this
   */
  AdvancedVertexBuffer pushShorts(VertexFormatElementType vertexFormatElementType, short... shorts);

  /**
   * Increment the vertex count by count.
   *
   * @param count the amount to increse the vertex count with
   * @return this
   */
  AdvancedVertexBuffer incrementVertexCount(int count);

  /**
   * see at {@link VertexBuffer#advanced()}.
   *
   * @return the simpler vertex buffer
   */
  VertexBuffer simple();

  /**
   * @return the current byteBuffer which stores all vertex information
   */
  ByteBuffer getByteBuffer();

  AdvancedVertexBuffer setByteBuffer(ByteBuffer byteBuffer);

  MatrixStack getMatrixStack();

}
