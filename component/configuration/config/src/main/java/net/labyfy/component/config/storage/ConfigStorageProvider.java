package net.labyfy.component.config.storage;

public interface ConfigStorageProvider extends ConfigStorage {

  void registerStorage(ConfigStorage storage);

}
