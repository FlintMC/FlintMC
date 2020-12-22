package net.flintmc.render.gui.webgui.internal;

import com.google.inject.Singleton;
import net.flintmc.render.gui.webgui.WebFileSystem;
import net.flintmc.render.gui.webgui.WebFileSystemHandler;
import net.flintmc.render.gui.webgui.WebResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/** Provides a web filesystem for reading class resources. */
@Singleton
@WebFileSystem("class-resource")
public class ResourceWebFileSystem implements WebFileSystemHandler {

  /** {@inheritDoc} */
  @Override
  public boolean existsFile(String path) {
    return getClass().getResource(path) != null;
  }

  /** {@inheritDoc} */
  @Override
  public WebResource getFile(final String path) throws FileNotFoundException {
    return new URLWebResource(path);
  }
}
