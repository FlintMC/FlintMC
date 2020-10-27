package net.flintmc.render.gui.webgui.internal;

import net.flintmc.render.gui.webgui.WebFileSystem;
import net.flintmc.render.gui.webgui.WebFileSystemHandler;
import net.flintmc.render.gui.webgui.WebResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** Provides a web filesystem for reading class resources. */
@WebFileSystem("class-resource")
public class ResourceWebFileSystem implements WebFileSystemHandler {

  private static final int BUFFER_SIZE = 512;

  /** {@inheritDoc} */
  @Override
  public boolean existsFile(String path) {
    return getClass().getResource(path) != null;
  }

  /** {@inheritDoc} */
  @Override
  public WebResource getFile(final String path) throws FileNotFoundException {
    return new WebResource() {

      private final URL url;
      private final List<Byte> data;
      int size = -1;
      private InputStream stream;

      {
        url = getClass().getResource("/" + path);
        data = new ArrayList<>();
        if (url == null) {
          throw new FileNotFoundException("Couldn't find resource at " + path);
        }
      }

      /** {@inheritDoc} */
      @Override
      public String getPath() {
        return path;
      }

      /** {@inheritDoc} */
      @Override
      public String getMimeType() {
        try {
          return Files.probeContentType(Paths.get(url.toURI()));
        } catch (IOException | URISyntaxException e) {
          throw new RuntimeException("What?", e);
        }
      }

      /** {@inheritDoc} */
      @Override
      public void open() throws IOException {
        stream = url.openStream();
      }

      /** {@inheritDoc} */
      @Override
      public void close() throws IOException {
        stream.close();
      }

      private int readFile() throws IOException {

        if (!data.isEmpty()) {
          return data.size();
        }
        int total = 0;
        int read;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((read = stream.read(buffer)) != -1) {
          total += read;
          for (int i = 0; i < read; i++) data.add(buffer[i]);
        }

        return total;
      }

      /** {@inheritDoc} */
      @Override
      public long getSize() throws IOException {
        if (size < 0) {
          size = readFile();
        }
        return size;
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
    };
  }
}
