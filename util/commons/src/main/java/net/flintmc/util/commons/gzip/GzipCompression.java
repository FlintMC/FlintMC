package net.flintmc.util.commons.gzip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompression {

  /**
   * Compresses a given input with the gzip compression algorithm.
   *
   * @param input the input to compress
   * @return the compressed data
   */
  public static byte[] compress(byte[] input) {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
      GZIPOutputStream gzip = new GZIPOutputStream(bos);
      gzip.write(input);
      gzip.close();
      byte[] compressed = bos.toByteArray();
      bos.close();
      return compressed;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return input;
  }

  /**
   * Decompress a given input with the gzip compression algorithm.
   *
   * @param input the input to decompress
   * @return the decompressed data
   */
  public static byte[] decompress(byte[] input) {
    try {
      byte[] buffer = new byte[1024];
      ByteArrayInputStream bis = new ByteArrayInputStream(input);
      GZIPInputStream gis = new GZIPInputStream(bis);
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      int len;
      while ((len = gis.read(buffer)) > 0) out.write(buffer, 0, len);

      gis.close();
      out.close();
      return out.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return input;
  }
}
