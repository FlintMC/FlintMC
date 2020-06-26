package net.labyfy.component.resources.pack;

import java.util.Collection;

/**
 * Service interface for retrieving enable resource packs.
 */
public interface ResourcePackProvider {
  /**
   * Retrieves a collection of all currently enabled resource packs.
   *
   * @return All enabled resource packs
   */
  Collection<ResourcePack> getEnabled();
}
