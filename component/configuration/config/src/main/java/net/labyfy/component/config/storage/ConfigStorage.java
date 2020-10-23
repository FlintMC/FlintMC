package net.labyfy.component.config.storage;

import net.labyfy.component.config.generator.ParsedConfig;

/**
 * A storage to store {@link ParsedConfig}s and read them again.
 *
 * @see StoragePriority
 */
public interface ConfigStorage {

  /**
   * Retrieves the name of this storage, unique per {@link ConfigStorageProvider}.
   *
   * @return The non-null name of this storage
   */
  String getName();

  /**
   * Instantly stores the given config (overrides if the config has already been stored).
   *
   * @param config The non-null config to be stored
   */
  void write(ParsedConfig config);

  /**
   * Instantly reads the given config, if the config has never been stored to this storage, nothing will happen.
   *
   * @param config The non-null config to be read
   */
  void read(ParsedConfig config);

}
