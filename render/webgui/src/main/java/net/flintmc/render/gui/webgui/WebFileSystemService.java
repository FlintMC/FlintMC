package net.flintmc.render.gui.webgui;

import net.flintmc.util.commons.Pair;

import java.util.Collection;

/** Holds a collection of discovered web filesystem implementations. */
public interface WebFileSystemService {

  /**
   * Gets a collection of web filesystem implementations and their protocol name. Creates the
   * instances via the injection holder if they haven't been created yet.
   *
   * @return a {@link Pair} with the first element being the filesystem instance and the second
   *     element being the protocol name of the filesystem
   */
  Collection<Pair<WebFileSystemHandler, String>> getFileSystems();
}
