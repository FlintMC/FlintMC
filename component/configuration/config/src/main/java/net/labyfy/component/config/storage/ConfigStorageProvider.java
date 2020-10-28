package net.labyfy.component.config.storage;

import javassist.CtClass;
import net.labyfy.component.config.annotation.ExcludeStorage;
import net.labyfy.component.config.annotation.IncludeStorage;
import net.labyfy.component.config.generator.ParsedConfig;

/**
 * Provider for all {@link ConfigStorage}s.
 */
public interface ConfigStorageProvider extends ConfigStorage {

  /**
   * Registers the given storage in this provider. The given storage needs to have a {@link StoragePriority} annotation.
   * If the annotation is missing, nothing will happen.
   *
   * @param storage The non-null storage to be registered
   * @throws IllegalStateException If a storage with the name inside of this storage already exists
   */
  void registerStorage(ConfigStorage storage) throws IllegalStateException;

  /**
   * Queues the given type to be registered as a storage. This doesn't register the storage directly, it will be
   * registered when it is needed by {@link #write(ParsedConfig)} or {@link #read(ParsedConfig)}. The given type needs
   * to implement {@link ConfigStorage}. Then the class will be loaded and an instance retrieved from the injector.
   * <p>
   * The given storage needs to have a {@link StoragePriority} annotation. If the annotation is missing, nothing will
   * happen.
   *
   * @param type The non-null type of the storage to be registered
   */
  void registerStorage(CtClass type);

  /**
   * Queues the given config to be stored in every storage registered in this provider, this doesn't store the config
   * instantly so that the config won't be stored multiple times just a few changes.
   *
   * @param config The non-null config to be stored
   */
  @Override
  void write(ParsedConfig config);

  /**
   * Instantly fills the given config with information stored in the registered storages in this provider. The storage
   * with the highest priority (which is {@link Integer#MIN_VALUE}) will be called last so that it overrides the other
   * storages. Only values that aren't ignored with {@link ExcludeStorage} and/or {@link IncludeStorage} for a storage
   * can be ignored.
   *
   * @param config The non-null config to be read
   */
  @Override
  void read(ParsedConfig config);
}
