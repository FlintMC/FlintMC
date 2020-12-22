package net.flintmc.render.gui.webgui.internal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import net.flintmc.render.gui.webgui.WebResource;

public class URLWebResource implements WebResource {

  private static final int BUFFER_SIZE = 512;

  private final String path;
  private final URL url;
  private final List<Byte> data;
  int size = -1;
  private InputStream stream;

  public URLWebResource(String path, URL url) {
    this.path = path;
    this.url = url;
    this.data = new ArrayList<>();
  }

  public URLWebResource(String path) throws FileNotFoundException {
    this(path, URLWebResource.class.getResource("/" + path));
    if (this.url == null) {
      throw new FileNotFoundException("Couldn't find resource at " + path);
    }
  }

  /** {@inheritDoc} */
  @Override
  public String getPath() {
    return this.path;
  }

  /** {@inheritDoc} */
  @Override
  public String getMimeType() {
    return URLConnection.guessContentTypeFromName(this.url.toString());
  }

  /** {@inheritDoc} */
  @Override
  public void open() throws IOException {
    this.stream = this.url.openStream();
  }

  /** {@inheritDoc} */
  @Override
  public void close() throws IOException {
    this.stream.close();
  }

  private int readFile() throws IOException {
    if (!this.data.isEmpty()) {
      return this.data.size();
    }
    int total = 0;
    int read;
    byte[] buffer = new byte[BUFFER_SIZE];
    while ((read = this.stream.read(buffer)) != -1) {
      total += read;
      for (int i = 0; i < read; i++) {
        this.data.add(buffer[i]);
      }
    }

    return total;
  }

  /** {@inheritDoc} */
  @Override
  public long getSize() throws IOException {
    if (this.size < 0) {
      this.size = readFile();
    }
    return this.size;
  }

  /** {@inheritDoc} */
  @Override
  public long readFromFile(ByteBuffer data, long length) throws IOException {
    int read = readFile();
    for (byte b : this.data) {
      data.put(b);
    }

    return read;
  }
}
