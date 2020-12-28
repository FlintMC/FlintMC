/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.v1_16_4.server.buffer;

import io.netty.buffer.Unpooled;
import java.util.UUID;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.server.buffer.PacketBuffer;

@Implement(value = PacketBuffer.class, version = "1.16.4")
public class VersionedPacketBuffer implements PacketBuffer {

  private final net.minecraft.network.PacketBuffer packetBuffer;

  @AssistedInject
  public VersionedPacketBuffer() {
    this.packetBuffer = new net.minecraft.network.PacketBuffer(Unpooled.buffer());
  }

  @AssistedInject
  public VersionedPacketBuffer(@Assisted("packetBuffer") Object buffer) {
    if (!(buffer instanceof net.minecraft.network.PacketBuffer)) {
      throw new IllegalArgumentException(
          buffer.getClass().getName()
              + " is not an instance of "
              + net.minecraft.network.PacketBuffer.class.getName());
    }
    this.packetBuffer = (net.minecraft.network.PacketBuffer) buffer;
  }

  @AssistedInject
  public VersionedPacketBuffer(@Assisted("buffer") byte[] buffer) {
    this.packetBuffer = new net.minecraft.network.PacketBuffer(Unpooled.wrappedBuffer(buffer));
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writeByteArray(byte[] data) {
    this.packetBuffer.writeByteArray(data);
    return this;
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
  public PacketBuffer writeVarInt(int input) {
    this.packetBuffer.writeVarInt(input);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public int readVarInt() {
    return this.packetBuffer.readVarInt();
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writeVarLong(long input) {
    this.packetBuffer.writeVarLong(input);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public long readVarLong() {
    return this.packetBuffer.readVarLong();
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writeUniqueIdentifier(UUID uniqueId) {
    this.packetBuffer.writeUniqueId(uniqueId);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public UUID readUniqueIdentifier() {
    return this.packetBuffer.readUniqueId();
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writeString(String content) {
    this.packetBuffer.writeString(content);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public String readString() {
    return this.packetBuffer.readString();
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writeString(String content, int maxLength) {
    this.packetBuffer.writeString(content, maxLength);
    return this;
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
  public PacketBuffer readerIndex(int readerIndex) {
    this.packetBuffer.readerIndex(readerIndex);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public int writerIndex() {
    return this.packetBuffer.writerIndex();
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writerIndex(int writerIndex) {
    this.packetBuffer.writerIndex(writerIndex);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public int capacity() {
    return this.packetBuffer.capacity();
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer capacity(int capacity) {
    this.packetBuffer.capacity(capacity);
    return this;
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
  public PacketBuffer ensureWritable(int minimumWritableBytes) {
    this.packetBuffer.ensureWritable(minimumWritableBytes);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writeBoolean(boolean data) {
    this.packetBuffer.writeBoolean(data);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public boolean readBoolean() {
    return this.packetBuffer.readBoolean();
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writeByte(byte data) {
    this.packetBuffer.writeByte(data);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public byte readByte() {
    return this.packetBuffer.readByte();
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writeShort(short data) {
    this.packetBuffer.writeShort(data);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public short readShort() {
    return this.packetBuffer.readShort();
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writeChar(char data) {
    this.packetBuffer.writeChar(data);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public char readChar() {
    return this.packetBuffer.readChar();
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writeInteger(int data) {
    this.packetBuffer.writeInt(data);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public int readInteger() {
    return this.packetBuffer.readInt();
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writeLong(long data) {
    this.packetBuffer.writeLong(data);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public long readLong() {
    return this.packetBuffer.readLong();
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writeFloat(float data) {
    this.packetBuffer.writeFloat(data);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public float readFloat() {
    return this.packetBuffer.readFloat();
  }

  /** {@inheritDoc} */
  @Override
  public PacketBuffer writeDouble(double data) {
    this.packetBuffer.writeDouble(data);
    return this;
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
