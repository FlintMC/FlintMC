package net.labyfy.component.gamesettings.configuration;

import java.util.List;

/**
 * Represents the resource pack configuration.
 */
public interface ResourcePackConfiguration {

  /**
   * Retrieves a collection with all resource packs.
   *
   * @return A collection with all resource packs.
   */
  List<String> getResourcePacks();

  /**
   * Changes the old resource pack collection with the new collection.
   *
   * @param resourcePacks The new resource pack collection.
   */
  void setResourcePacks(List<String> resourcePacks);

  /**
   * Retrieves a collection with all incompatible resource packs.
   *
   * @return A collection with all incompatible resource packs.
   */
  List<String> getIncompatibleResourcePacks();

  /**
   * Changes the old incompatible resource pack collection with the new collection.
   *
   * @param incompatibleResourcePacks The new incompatible resource pack colleciton.
   */
  void setIncompatibleResourcePacks(List<String> incompatibleResourcePacks);

}
