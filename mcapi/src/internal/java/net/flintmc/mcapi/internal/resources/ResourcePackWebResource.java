package net.flintmc.mcapi.internal.resources;

import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.render.gui.webgui.WebResource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ResourcePackWebResource implements WebResource {

  private static final int BUFFER_SIZE = 512;

  private final ResourceLocation location;
  private final List<Byte> data;
  private InputStream stream;
  private long size;

  protected ResourcePackWebResource(ResourceLocation location) {
    this.location = location;
    this.data = new ArrayList<>();
  }

  /** {@inheritDoc} */
  @Override
  public String getPath() {
    return this.location.getNamespace() + ':' + this.location.getPath();
  }

  /** {@inheritDoc} */
  @Override
  public String getMimeType() {
    return URLConnection.guessContentTypeFromName(this.location.getPath());
  }

  /** {@inheritDoc} */
  @Override
  public void open() throws IOException {
    this.stream = this.location.openInputStream();
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
