package net.flintmc.mcapi.entity.type;

import java.util.Map;

/** The register is used to register {@link EntityType}'s or to get {@link EntityType}'s. */
public interface EntityTypeRegister {

  /**
   * Retrieves a key-value system with the registered {@link EntityType}'s.
   *
   * @return A key-value system.
   */
  Map<String, EntityType> getEntityTypes();

  /**
   * Retrieves the entity type by the given key.
   *
   * @param key The key to get an entity type.
   * @return An entity type or a default value.
   */
  EntityType getEntityType(String key);
}
