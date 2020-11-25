package net.flintmc.mcapi.server.buffer;

public interface PacketBuffer {

  PacketBuffer writeByteArray(byte[] array);

  byte[] readByteArray();

  byte[] readByteArray(int maxLength);

}
