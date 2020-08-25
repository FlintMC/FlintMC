package net.labyfy.component.mappings.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * I/O utility class for mapping parser.
 */
public final class IOUtils {
  private IOUtils() { }

  /**
   * Read an input stream to string.
   *
   * @param inputStream An input stream.
   * @return A string based on the input stream data.
   * @throws IOException If the input stream cannot be read.
   */
  public static String readToString(InputStream inputStream) throws IOException {
    StringBuilder builder = new StringBuilder();
    byte[] buffer = new byte[4096];
    int length;

    while ((length = inputStream.read(buffer)) > 0) {
      builder.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
    }

    inputStream.close();
    return builder.toString();
  }
}
