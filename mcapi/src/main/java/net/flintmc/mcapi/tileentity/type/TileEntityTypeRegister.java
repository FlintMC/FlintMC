package net.flintmc.mcapi.tileentity.type;

import java.util.Map;

/** The register is used to register {@link TileEntityType}'s or to get {@link TileEntityType}'s. */
public interface TileEntityTypeRegister {

  /**
   * Retrieves a key-value system with the registered {@link TileEntityType}'s.
   *
   * @return A key-value system.
   */
  Map<String, TileEntityType> getTileEntitiesTypes();

  /**
   * Retrieves the tile entity type by teh given key.
   *
   * @param key The key to get an tile entity type.
   * @return A tile entity type or a default value.
   */
  TileEntityType getTileEntityType(String key);
}
