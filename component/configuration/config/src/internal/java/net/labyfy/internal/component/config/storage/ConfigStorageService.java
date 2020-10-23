package net.labyfy.internal.component.config.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.generator.ConfigGenerator;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.storage.ConfigStorage;
import net.labyfy.component.config.storage.ConfigStorageProvider;
import net.labyfy.component.config.storage.LoadStorage;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;

import java.util.Collection;

@Singleton
@Service(value = LoadStorage.class, priority = 1 /* higher than from the ConfigGenerator so that the configs are available for reading in this service */)
public class ConfigStorageService implements ServiceHandler {

  private final ConfigGenerator generator;
  private final ConfigStorageProvider provider;

  @Inject
  public ConfigStorageService(ConfigGenerator generator, ConfigStorageProvider provider) {
    this.generator = generator;
    this.provider = provider;
  }

  @Override
  public void discover(Identifier.Base property) {
    Class<? extends ConfigStorage> location = property.getProperty().getLocatedIdentifiedAnnotation().getLocation();
    ConfigStorage storage = InjectionHolder.getInjectedInstance(location);

    this.provider.registerStorage(storage);

    Collection<ParsedConfig> configs = this.generator.getDiscoveredConfigs();
    if (!configs.isEmpty()) {
      // read the configs from the storage

      for (ParsedConfig config : configs) {
        storage.read(config);
      }
    }
  }
}
