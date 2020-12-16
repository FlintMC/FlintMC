package net.flintmc.mcapi.resources.pack;

import java.util.List;

/**
 * Service interface for retrieving enable resource packs.
 */
public interface ResourcePackProvider {

  /**
   * Retrieves a collection of all currently enabled resource packs.
   *
   * @return All enabled resource packs
   */
  List<ResourcePack> getEnabled();

  /**
   * Retrieves a collection of all available resource packs.
   *
   * @return All available resource packs
   */
  List<ResourcePack> getAvailable();
}
