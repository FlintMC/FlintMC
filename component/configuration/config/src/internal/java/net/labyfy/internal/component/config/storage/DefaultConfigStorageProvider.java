package net.labyfy.internal.component.config.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import net.labyfy.component.config.event.ConfigStorageEvent;
import net.labyfy.component.config.generator.ConfigGenerator;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.storage.ConfigStorage;
import net.labyfy.component.config.storage.ConfigStorageProvider;
import net.labyfy.component.config.storage.StoragePriority;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.inject.primitive.InjectionHolder;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Implement(ConfigStorageProvider.class)
public class DefaultConfigStorageProvider implements ConfigStorageProvider {

  private static final String NAME = "default";

  private final Logger logger;
  private final EventBus eventBus;
  private final ConfigStorageEvent.Factory eventFactory;

  private final ConfigGenerator configGenerator;

  private final List<ComparableConfigStorage> storages = new ArrayList<>();
  private final Map<Class<?>, ParsedConfig> pendingWrites = new ConcurrentHashMap<>();

  private final Collection<CtClass> pendingStorages = new CopyOnWriteArrayList<>();

  @Inject
  public DefaultConfigStorageProvider(@InjectLogger Logger logger, ConfigStorageEvent.Factory eventFactory,
                                      EventBus eventBus, ScheduledExecutorService executorService,
                                      ConfigGenerator configGenerator) {
    this.logger = logger;
    this.eventFactory = eventFactory;
    this.eventBus = eventBus;
    this.configGenerator = configGenerator;
    executorService.scheduleAtFixedRate(() -> {
      if (this.pendingWrites.isEmpty()) {
        return;
      }

      this.processPendingStorages();

      try {
        Map<Class<?>, ParsedConfig> copy = new HashMap<>(this.pendingWrites);
        this.pendingWrites.clear();

        for (ParsedConfig config : copy.values()) {
          ConfigStorageEvent event = this.eventFactory.create(ConfigStorageEvent.Type.WRITE, config);
          this.eventBus.fireEvent(event, Subscribe.Phase.PRE);

          for (ConfigStorage storage : this.storages) {
            storage.write(config);
          }

          this.eventBus.fireEvent(event, Subscribe.Phase.POST);
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
    this.processPendingStorages();

    ConfigStorageEvent event = this.eventFactory.create(ConfigStorageEvent.Type.READ, config);
    this.eventBus.fireEvent(event, Subscribe.Phase.PRE);

    for (ComparableConfigStorage storage : this.storages) {
      storage.read(config);
    }

    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
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

    Collection<ParsedConfig> configs = this.configGenerator.getDiscoveredConfigs();
    if (!configs.isEmpty()) {
      // read the configs from the storage

      for (ParsedConfig config : configs) {
        storage.read(config);
      }
    }
  }

  @Override
  public void registerStorage(CtClass type) {
    this.pendingStorages.add(type);
  }

  private void processPendingStorages() {
    if (this.pendingStorages.isEmpty()) {
      return;
    }

    for (CtClass type : this.pendingStorages) {
      try {
        Class<?> configClass = super.getClass().getClassLoader().loadClass(type.getName());
        if (!ConfigStorage.class.isAssignableFrom(configClass)) {
          this.logger.trace("Failed to load config " + type.getName() + ": Class doesn't implement " + ConfigStorage.class.getName());
          continue;
        }

        ConfigStorage storage = (ConfigStorage) InjectionHolder.getInjectedInstance(configClass);
        this.registerStorage(storage);
      } catch (ClassNotFoundException | IllegalStateException e) {
        this.logger.error("Failed to load config " + type.getName(), e);
      }
    }

    this.pendingStorages.clear();
  }

}
