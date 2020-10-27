package net.flintmc.mcapi.resources.pack;

import net.flintmc.mcapi.resources.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Represents a minecraft resource pack.
 */
public interface ResourcePack {
  /**
   * Retrieves all namespaces contained in this resource pack.
   *
   * @return All namespaces this resource pack contains
   */
  Collection<String> getNameSpaces();

  /**
   * Retrieves the name of the resource pack.
   *
   * @return The name of the resource pack
   */
  String getName();

  /**
   * Opens an input stream to a specific resource location in this resource pack.
   * Usually {@link ResourceLocation#openInputStream()} should be preferred as it will respect
   * the currently selected resource pack, which this method does not.
   *
   * @param resourceLocation The resource location to open the stream to
   * @return A stream which points to the given resource location
   * @throws java.io.FileNotFoundException If the given resource location can't be found in the pack
   * @throws IOException                   If an I/O error occurs
   */
  InputStream getStream(ResourceLocation resourceLocation) throws IOException;

  /**
   * Retrieves the description of the resource pack.
   *
   * @return The description of the resource pack
   */
  String getDescription();

  /**
   * Retrieves the title of the resource pack.
   *
   * @return The title of the resource pack
   */
  String getTitle();
}
