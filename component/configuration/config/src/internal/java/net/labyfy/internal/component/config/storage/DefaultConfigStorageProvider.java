package net.labyfy.internal.component.config.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.storage.ConfigStorage;
import net.labyfy.component.config.storage.ConfigStorageProvider;
import net.labyfy.component.config.storage.StoragePriority;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Implement(ConfigStorageProvider.class)
public class DefaultConfigStorageProvider implements ConfigStorageProvider {

  private static final String NAME = "default";

  private final Logger logger;

  private final List<ComparableConfigStorage> storages = new ArrayList<>();

  private final Map<Class<?>, ParsedConfig> pendingWrites = new ConcurrentHashMap<>();

  @Inject
  public DefaultConfigStorageProvider(@InjectLogger Logger logger, ScheduledExecutorService executorService) {
    this.logger = logger;
    executorService.scheduleAtFixedRate(() -> {
      if (this.pendingWrites.isEmpty()) {
        return;
      }

      try {
        Map<Class<?>, ParsedConfig> copy = new HashMap<>(this.pendingWrites);
        this.pendingWrites.clear();

        for (ParsedConfig config : copy.values()) {
          for (ConfigStorage storage : this.storages) {
            storage.write(config);
          }
        }
      } catch (Throwable throwable) {
        this.logger.error("Failed to write a config to the storage", throwable);
      }

    }, 1, 1, TimeUnit.SECONDS);
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public void write(ParsedConfig config) {
    this.pendingWrites.put(config.getClass(), config);
  }

  @Override
  public void read(ParsedConfig config) {
    for (ComparableConfigStorage storage : this.storages) {
      storage.read(config);
    }
  }

  @Override
  public void registerStorage(ConfigStorage storage) throws IllegalStateException {
    if (!storage.getClass().isAnnotationPresent(StoragePriority.class)) {
      return;
    }

    int priority = storage.getClass().getAnnotation(StoragePriority.class).value();
    String name = storage.getName();

    for (ComparableConfigStorage registered : this.storages) {
      if (registered.getName().equals(name)) {
        throw new IllegalStateException("A storage with the name '" + name + "' is already registered");
      }
    }

    this.storages.add(ComparableConfigStorage.wrap(storage, priority));
    this.storages.sort(Collections.reverseOrder());
  }
}
