package net.labyfy.component.render;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.nio.ByteBuffer;

/**
 * A {@link VertexFormat} defines the structure for a collection of vertices.
 * see at {@link VertexFormatElement}
 */
public interface VertexFormat {

  /**
   * Pushes floats to the current vertex and checks for the existence of the {@link VertexFormatElement}.
   *
   * @param byteBuffer   the bytebuffer to push the data to
   * @param vertexBuffer the vertexBuffer to fetch the current vertex data from
   * @param floats       data to push
   * @return this
   */
  VertexFormat pushFloats(ByteBuffer byteBuffer, VertexBuffer vertexBuffer, VertexFormatElementType vertexFormatElementType, float... floats);

  /**
   * Pushes bytes to the current vertex and checks for the existence of the {@link VertexFormatElement}.
   *
   * @param byteBuffer   the bytebuffer to push the data to
   * @param vertexBuffer the vertexBuffer to fetch the current vertex data from
   * @param bytes        data to push
   * @return this
   */
  VertexFormat pushBytes(ByteBuffer byteBuffer, VertexBuffer vertexBuffer, VertexFormatElementType vertexFormatElementType, byte... bytes);

  /**
   * Pushes shorts to the current vertex and checks for the existence of the {@link VertexFormatElement}.
   *
   * @param byteBuffer   the bytebuffer to push the data to
   * @param vertexBuffer the vertexBuffer to fetch the current vertex data from
   * @param shorts       data to push
   * @return this
   */
  VertexFormat pushShorts(ByteBuffer byteBuffer, VertexBuffer vertexBuffer, VertexFormatElementType vertexFormatElementType, short... shorts);


  /**
   * Pushes bytes to the current vertex and checks for the existence of the {@link VertexFormatElement}.
   *
   * @param byteBuffer   the bytebuffer to push the data to
   * @param vertexBuffer the vertexBuffer to fetch the current vertex data from
   * @param bytes        data to push
   * @return this
   */
  VertexFormat pushBytes(ByteBuffer byteBuffer, VertexBuffer vertexBuffer, VertexFormatElementType vertexFormatElementType, ByteBuffer bytes);

  /**
   * @return all elements of this vertex format
   */
  VertexFormatElement[] getElements();

  /**
   * @param vertexFormatElementType the type to search the format element from
   * @return if this vertex format has an format element with the name name
   */
  boolean hasElement(VertexFormatElementType vertexFormatElementType);

  /**
   * @param vertexFormatElementType the type to search the format element from
   * @return the vertex format element by name
   */
  VertexFormatElement getElement(VertexFormatElementType vertexFormatElementType);

  /**
   * @return the size of all {@link VertexFormatElement} together in bytes.
   */
  int getSize();

  /**
   * Every {@link VertexFormatElement} gets a byte offset in the context of a {@link VertexFormat}.
   * The byte offset is the index in the current already allocated vertex data where the data for the {@link VertexFormatElement} should be written at.
   *
   * @param vertexFormatElementType the type of {@link VertexFormatElement} to look for
   * @return the byte offset
   */
  int getByteOffset(VertexFormatElementType vertexFormatElementType);

  <T> T getHandle();


  @AssistedFactory(VertexFormat.class)
  interface Factory {
    VertexFormat create(@Assisted VertexFormatElement... elements);
  }

}
