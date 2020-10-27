package net.flintmc.render.gui.webgui;

import java.io.FileNotFoundException;

/** Represents a web file system that can be used by the web front-end. */
public interface WebFileSystemHandler {

  /**
   * Check if file path exists.
   *
   * @param path The path to check for a file at
   * @return {@code true} if the file exists, {@code false} otherwise
   */
  boolean existsFile(String path);

  /**
   * Gets a {@link WebResource}.
   *
   * @param path The path to the resource
   * @return the resource
   * @throws FileNotFoundException if no resource was found at the given path
   */
  WebResource getFile(String path) throws FileNotFoundException;
}
