package net.labyfy.webgui;

import java.io.IOException;
import java.nio.ByteBuffer;

/** Represents a resource readable from a web front-end. */
public interface WebResource {

  /** @return the path to this resource */
  String getPath();

  /** @return the MIME type of this resource */
  String getMimeType();

  /**
   * Opens this resource for reading
   *
   * @throws IOException if the resource couldn't be opened
   */
  void open() throws IOException;

  /**
   * Closes this resource for reading
   *
   * @throws IOException if the resource couldn't be closed
   */
  void close() throws IOException;

  /**
   * @return the size of this resource's content in bytes
   * @throws IOException if the size couldn't be obtained
   */
  long getSize() throws IOException;

  /**
   * Reads from this resource
   *
   * @param data the buffer to write the resource content to
   * @param length the maximum number of bytes to read
   * @return the number of bytes read (and written to data)
   * @throws IOException if the resource couldn't be read
   */
  long readFromFile(ByteBuffer data, long length) throws IOException;
}
