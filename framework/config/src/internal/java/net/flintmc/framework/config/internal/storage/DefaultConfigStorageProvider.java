/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.framework.config.internal.storage;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.event.ConfigStorageEvent;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.storage.ConfigStorage;
import net.flintmc.framework.config.storage.ConfigStorageProvider;
import net.flintmc.framework.config.storage.StoragePriority;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Implement(ConfigStorageProvider.class)
public class DefaultConfigStorageProvider implements ConfigStorageProvider {

  private static final String NAME = "default";

  private final Logger logger;
  private final EventBus eventBus;
  private final ConfigStorageEvent.Factory eventFactory;

  private final List<ComparableConfigStorage> storages = new ArrayList<>();
  private final Map<Class<?>, ParsedConfig> pendingWrites = new ConcurrentHashMap<>();

  @Inject
  private DefaultConfigStorageProvider(
      @InjectLogger Logger logger,
      ConfigStorageEvent.Factory eventFactory,
      EventBus eventBus,
      ScheduledExecutorService executorService) {
    this.logger = logger;
    this.eventFactory = eventFactory;
    this.eventBus = eventBus;
    executorService.scheduleAtFixedRate(
        () -> {
          if (this.pendingWrites.isEmpty()) {
            return;
          }

          try {
            Map<Class<?>, ParsedConfig> copy = new HashMap<>(this.pendingWrites);
            this.pendingWrites.clear();

            for (ParsedConfig config : copy.values()) {
              ConfigStorageEvent event =
                  this.eventFactory.create(ConfigStorageEvent.Type.WRITE, config);
              this.eventBus.fireEvent(event, Subscribe.Phase.PRE);

              for (ConfigStorage storage : this.storages) {
                storage.write(config);
              }

              this.eventBus.fireEvent(event, Subscribe.Phase.POST);
            }
          } catch (Throwable throwable) {
            this.logger.error("Failed to write a config to the storage", throwable);
          }
        },
        1,
        1,
        TimeUnit.SECONDS);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void write(ParsedConfig config) {
    if (config.isInitialized()) {
      this.pendingWrites.put(config.getClass(), config);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void read(ParsedConfig config) {
    ConfigStorageEvent event = this.eventFactory.create(ConfigStorageEvent.Type.READ, config);
    this.eventBus.fireEvent(event, Subscribe.Phase.PRE);

    for (ComparableConfigStorage storage : this.storages) {
      storage.read(config);
    }

    this.eventBus.fireEvent(event, Subscribe.Phase.POST);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerStorage(ConfigStorage storage) throws IllegalStateException {
    if (!storage.getClass().isAnnotationPresent(StoragePriority.class)) {
      return;
    }

    int priority = storage.getClass().getAnnotation(StoragePriority.class).value();
    String name = storage.getName();

    for (ComparableConfigStorage registered : this.storages) {
      if (registered.getName().equals(name)) {
        throw new IllegalStateException(
            "A storage with the name '" + name + "' is already registered");
      }
    }

    this.storages.add(ComparableConfigStorage.wrap(storage, priority));
    Collections.sort(this.storages); // load the higher priorities last
  }
}
