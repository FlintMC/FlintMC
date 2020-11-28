package net.flintmc.mcapi.server.buffer;

import java.util.UUID;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

// TODO: 26.11.2020 Add more logic to the packet buffer
/** Represents a random and sequential accessible sequence of zero or more bytes. */
public interface PacketBuffer {

  /**
   * Writes a byte array into this buffer.
   *
   * @param data The byte array to be written.
   */
  void writeByteArray(byte[] data);

  /**
   * Reads a byte array from this buffer.
   *
   * @return The read byte array.
   */
  byte[] readByteArray();

  /**
   * Reads a byte array from this buffer.
   *
   * @param maxLength The maximum length of the byte array.
   * @return The read byte array.
   */
  byte[] readByteArray(int maxLength);

  /**
   * Writes a var-int into this buffer.
   *
   * @param input The input to be written.
   */
  void writeVarInt(int input);

  /**
   * Reads a var-int from this buffer.
   *
   * @return The read var-int.
   */
  int readVarInt();

  /**
   * Writes a var-long into this buffer.
   *
   * @param input The input to be written.
   */
  void writeVarLong(long input);

  /**
   * Reads a var-long from this buffer.
   *
   * @return The read var-long.
   */
  long readVarLong();

  /**
   * Writes an unique identifier into this buffer.
   *
   * @param uniqueId The unique identifier to be written.
   */
  void writeUniqueIdentifier(UUID uniqueId);

  /**
   * Reads an unique identifier from this buffer.
   *
   * @return The read unique identifier.
   */
  UUID readUniqueIdentifier();

  /**
   * Writes a string into this buffer.
   *
   * @param content The content to be written.
   */
  void writeString(String content);

  /**
   * Reads a string from this buffer.
   *
   * @return A read string.
   * @see #readString(int)
   */
  String readString();

  /**
   * Writes a string into this buffer.
   *
   * @param content The content to be written.
   * @param maxLength The maximum length of the content.
   */
  void writeString(String content, int maxLength);

  /**
   * Reads a string from this buffer.
   *
   * @param maxLength The maximum length of the string.
   * @return The read string.
   */
  String readString(int maxLength);

  /**
   * Retrieves the reader index of this buffer.
   *
   * @return The reader index of this buffer.
   */
  int readerIndex();

  /**
   * Sets the reader index of this buffer.
   *
   * @param readerIndex The new reader index.
   * @throws IndexOutOfBoundsException If the specified {@code readerIndex} is less than {@code 0}
   *     or greater than {@link #writerIndex()}.
   */
  void readerIndex(int readerIndex);

  /**
   * Retrieves the writer index of this buffer.
   *
   * @return The writer index of this buffer.
   */
  int writerIndex();

  /**
   * Sets the writer index of this buffer.
   *
   * @param writerIndex The new writer index.
   * @throws IndexOutOfBoundsException If the specified {@code writerIndex} is less than {@link
   *     #readerIndex()} or greater than {@link #capacity()}.
   */
  void writerIndex(int writerIndex);

  /**
   * Retrieves the number of bytes this buffer can contain.
   *
   * @return The number of bytes this buffer can contain.
   */
  int capacity();

  /**
   * Adjusts the capacity of this buffer. If the {@code capacity} is less than the current capacity,
   * the content of this buffer is truncated. If the {@code capacity} is greater thant the current
   * capacity, ths buffer is appended with unspecified data whose length is ({@code capacity} -
   * currentCapacity)
   *
   * @param capacity The new capacity.
   */
  void capacity(int capacity);

  /**
   * Retrieves the maximum allowed capacity of this buffer. If a user attempts to increase the
   * capacity of this buffer beyond the maximum capacity using {@link #capacity(int)} or {@link
   * #ensureWritable(int)}, those method will raise an {@link IllegalArgumentException}.
   *
   * @return The maximum allowed capacity of this buffer.
   */
  int maximumCapacity();

  /**
   * Retrieves the number of readable bytes which is equal to the {@code writerIndex - readerIndex}
   *
   * @return The number of readable bytes.
   */
  int readableBytes();

  /**
   * Retrieves the number of writable bytes which is equal to {@code capacity - writerIndex}.
   *
   * @return The number of writable bytes.
   */
  int writableBytes();

  /**
   * Makes sure the number of the writable bytes is equal to or greater than specified value. If
   * there is enough writable bytes in this buffer, this method returns with no side effect.
   * Otherwise, it raises an {@link IllegalArgumentException}.
   *
   * @param minimumWritableBytes The expected minimum number of writeable bytes.
   * @throws IndexOutOfBoundsException If {@link #writerIndex()} + {@code minimumWritableBytes} &gt;
   *     {@link #maximumCapacity()}
   */
  void ensureWritable(int minimumWritableBytes);

  /**
   * Writes a boolean into this buffer.
   *
   * @param data The boolean to be written.
   */
  void writeBoolean(boolean data);

  /**
   * Reads a boolean from this buffer.
   *
   * @return The read boolean.
   */
  boolean readBoolean();

  /**
   * Writes a byte into this buffer.
   *
   * @param data The byte to be written.
   */
  void writeByte(byte data);

  /**
   * Reads a byte from this buffer.
   *
   * @return The read byte.
   */
  byte readByte();

  /**
   * Writes a short into this buffer.
   *
   * @param data The short to be written.
   */
  void writeShort(short data);

  /**
   * Reads a short from this buffer.
   *
   * @return The read short.
   */
  short readShort();

  /**
   * Writes a char into this buffer.
   *
   * @param data The char to be written.
   */
  void writeChar(char data);

  /**
   * Reads a char from this buffer.
   *
   * @return The read char.
   */
  char readChar();

  /**
   * Writes an integer into this buffer.
   *
   * @param data The integer to be written.
   */
  void writeInteger(int data);

  /**
   * Reads an integer from this buffer.
   *
   * @return The read integer.
   */
  int readInteger();

  /**
   * Writes a long into this buffer.
   *
   * @param data The long to be written.
   */
  void writeLong(long data);

  /**
   * Reads a long from this buffer.
   *
   * @return The read long.
   */
  long readLong();

  /**
   * Writes a float into this buffer.
   *
   * @param data The float to be written.
   */
  void writeFloat(float data);

  /**
   * Reads a float from this buffer.
   *
   * @return The read float.
   */
  float readFloat();

  /**
   * Writes a double into this buffer.
   *
   * @param data The double to be written.
   */
  void writeDouble(double data);

  /**
   * Reads a double from this buffer.
   *
   * @return The read double.
   */
  double readDouble();

  /**
   * Retrieves the backing byte array of this buffer.
   *
   * @return The backing byte array of this buffer.
   * @throws UnsupportedOperationException If there no accessible backing byte array.
   */
  byte[] array();

  /** A factory class for the {@link PacketBuffer}. */
  @AssistedFactory(PacketBuffer.class)
  interface Factory {

    /**
     * Creates a new {@link PacketBuffer}.
     *
     * @return A created packet buffer.
     */
    PacketBuffer create();

    /**
     * Creates a new {@link PacketBuffer} with the given object.
     *
     * @param buffer The non-null Minecraft packet buffer.
     * @return A created packet buffer.
     */
    PacketBuffer create(@Assisted("packetBuffer") Object buffer);

    /**
     * Creates a new {@link PacketBuffer} with the given byte array.
     *
     * @param buffer The non-null Minecraft packet buffer.
     * @return A created packet buffer.
     */
    PacketBuffer create(@Assisted("buffer") byte[] buffer);
  }
}
