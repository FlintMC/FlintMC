package net.flintmc.mcapi.v1_15_2.server.buffer;

import io.netty.buffer.Unpooled;
import java.util.UUID;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.buffer.PacketBuffer;

@Implement(PacketBuffer.class)
public class VersionedPacketBuffer implements PacketBuffer {

  private final net.minecraft.network.PacketBuffer packetBuffer;

  @AssistedInject
  public VersionedPacketBuffer() {
    this.packetBuffer = new net.minecraft.network.PacketBuffer(Unpooled.buffer());
  }

  @AssistedInject
  public VersionedPacketBuffer(@Assisted("packetBuffer") Object buffer) {
    if (!(buffer instanceof net.minecraft.network.PacketBuffer)) {
      throw new IllegalArgumentException("");
    }
    this.packetBuffer = (net.minecraft.network.PacketBuffer) buffer;
  }

  @AssistedInject
  public VersionedPacketBuffer(@Assisted("buffer") byte[] buffer) {
    this.packetBuffer = new net.minecraft.network.PacketBuffer(Unpooled.wrappedBuffer(buffer));
  }

  /** {@inheritDoc} */
  @Override
  public void writeByteArray(byte[] data) {
    this.packetBuffer.writeByteArray(data);
  }

  /** {@inheritDoc} */
  @Override
  public byte[] readByteArray() {
    return this.packetBuffer.readByteArray();
  }

  /** {@inheritDoc} */
  @Override
  public byte[] readByteArray(int maxLength) {
    return this.packetBuffer.readByteArray(maxLength);
  }

  /** {@inheritDoc} */
  @Override
  public void writeVarInt(int input) {
    this.packetBuffer.writeVarInt(input);
  }

  /** {@inheritDoc} */
  @Override
  public int readVarInt() {
    return this.packetBuffer.readVarInt();
  }

  /** {@inheritDoc} */
  @Override
  public void writeVarLong(long input) {
    this.packetBuffer.writeVarLong(input);
  }

  /** {@inheritDoc} */
  @Override
  public long readVarLong() {
    return this.packetBuffer.readVarLong();
  }

  /** {@inheritDoc} */
  @Override
  public void writeUniqueIdentifier(UUID uniqueId) {
    this.packetBuffer.writeUniqueId(uniqueId);
  }

  /** {@inheritDoc} */
  @Override
  public UUID readUniqueIdentifier() {
    return this.packetBuffer.readUniqueId();
  }

  /** {@inheritDoc} */
  @Override
  public void writeString(String content) {
    this.packetBuffer.writeString(content);
  }

  /** {@inheritDoc} */
  @Override
  public String readString() {
    return this.packetBuffer.readString();
  }

  /** {@inheritDoc} */
  @Override
  public void writeString(String content, int maxLength) {
    this.packetBuffer.writeString(content, maxLength);
  }

  /** {@inheritDoc} */
  @Override
  public String readString(int maxLength) {
    return this.packetBuffer.readString(maxLength);
  }

  /** {@inheritDoc} */
  @Override
  public int readerIndex() {
    return this.packetBuffer.readerIndex();
  }

  /** {@inheritDoc} */
  @Override
  public void readerIndex(int readerIndex) {
    this.packetBuffer.readerIndex(readerIndex);
  }

  /** {@inheritDoc} */
  @Override
  public int writerIndex() {
    return this.packetBuffer.writerIndex();
  }

  /** {@inheritDoc} */
  @Override
  public void writerIndex(int writerIndex) {
    this.packetBuffer.writerIndex(writerIndex);
  }

  /** {@inheritDoc} */
  @Override
  public int capacity() {
    return this.packetBuffer.capacity();
  }

  /** {@inheritDoc} */
  @Override
  public void capacity(int capacity) {
    this.packetBuffer.capacity(capacity);
  }

  /** {@inheritDoc} */
  @Override
  public int maximumCapacity() {
    return this.packetBuffer.maxCapacity();
  }

  /** {@inheritDoc} */
  @Override
  public int readableBytes() {
    return this.packetBuffer.readableBytes();
  }

  /** {@inheritDoc} */
  @Override
  public int writableBytes() {
    return this.packetBuffer.writableBytes();
  }

  /** {@inheritDoc} */
  @Override
  public void ensureWritable(int minimumWritableBytes) {
    this.packetBuffer.ensureWritable(minimumWritableBytes);
  }

  /** {@inheritDoc} */
  @Override
  public void writeBoolean(boolean data) {
    this.packetBuffer.writeBoolean(data);
  }

  /** {@inheritDoc} */
  @Override
  public boolean readBoolean() {
    return this.packetBuffer.readBoolean();
  }

  /** {@inheritDoc} */
  @Override
  public void writeByte(byte data) {
    this.packetBuffer.writeByte(data);
  }

  /** {@inheritDoc} */
  @Override
  public byte readByte() {
    return this.packetBuffer.readByte();
  }

  /** {@inheritDoc} */
  @Override
  public void writeShort(short data) {
    this.packetBuffer.writeShort(data);
  }

  /** {@inheritDoc} */
  @Override
  public short readShort() {
    return this.packetBuffer.readShort();
  }

  /** {@inheritDoc} */
  @Override
  public void writeChar(char data) {
    this.packetBuffer.writeChar(data);
  }

  /** {@inheritDoc} */
  @Override
  public char readChar() {
    return this.packetBuffer.readChar();
  }

  /** {@inheritDoc} */
  @Override
  public void writeInteger(int data) {
    this.packetBuffer.writeInt(data);
  }

  /** {@inheritDoc} */
  @Override
  public int readInteger() {
    return this.packetBuffer.readInt();
  }

  /** {@inheritDoc} */
  @Override
  public void writeLong(long data) {
    this.packetBuffer.writeLong(data);
  }

  /** {@inheritDoc} */
  @Override
  public long readLong() {
    return this.packetBuffer.readLong();
  }

  /** {@inheritDoc} */
  @Override
  public void writeFloat(float data) {
    this.packetBuffer.writeFloat(data);
  }

  /** {@inheritDoc} */
  @Override
  public float readFloat() {
    return this.packetBuffer.readFloat();
  }

  /** {@inheritDoc} */
  @Override
  public void writeDouble(double data) {
    this.packetBuffer.writeDouble(data);
  }

  /** {@inheritDoc} */
  @Override
  public double readDouble() {
    return this.packetBuffer.readDouble();
  }

  @Override
  public byte[] array() {
    return this.packetBuffer.array();
  }
}
