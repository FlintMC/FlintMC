package net.flintmc.util.mojang.internal.cache;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class DataStreamHelper {

  private DataStreamHelper() {
    throw new UnsupportedOperationException();
  }

  public static void writeString(DataOutput output, String value) throws IOException {
    output.writeShort(value.length());
    output.write(value.getBytes(StandardCharsets.UTF_8));
  }

  public static String readString(DataInput input) throws IOException {
    int length = input.readShort();
    if (length < 0) {
      throw new IllegalArgumentException("Invalid array length: " + length);
    }
    byte[] data = new byte[length];
    input.readFully(data);
    return new String(data, StandardCharsets.UTF_8);
  }
}
