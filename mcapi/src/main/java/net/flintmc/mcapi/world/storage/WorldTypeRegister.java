package net.flintmc.mcapi.world.storage;

import java.util.List;

/**
 * Represents a world type  register.
 */
public interface WorldTypeRegister {

  /**
   * Retrieves a collection with all world types.
   *
   * @return A collection with all world types.
   */
  List<WorldType> getWorldTypes();

}
