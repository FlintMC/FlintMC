package net.flintmc.framework.config.internal;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.EventConfigInitializer;
import net.flintmc.framework.config.annotation.ConfigInit;
import net.flintmc.framework.config.generator.ConfigGenerator;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.util.commons.Pair;

/** {@inheritDoc} */
@Singleton
@Implement(EventConfigInitializer.class)
public class DefaultEventConfigInitializer implements EventConfigInitializer {

  private final ConfigGenerator configGenerator;
  private final Multimap<Class<?>, Pair<Subscribe.Phase, ParsedConfig>>
      pendingConfigInitializations = HashMultimap.create();

  @Inject
  public DefaultEventConfigInitializer(ConfigGenerator configGenerator) {
    this.configGenerator = configGenerator;
  }

  /** {@inheritDoc} */
  @Override
  public void addPendingInitialization(ParsedConfig config, ConfigInit configInit) {
    this.pendingConfigInitializations.put(
        configInit.eventClass(), new Pair<>(configInit.eventPhase(), config));
  }

  @Subscribe(phase = Subscribe.Phase.PRE)
  public void readEventConfigsPre(Event event) {
    this.readEventConfigs(event, Subscribe.Phase.PRE);
  }

  @Subscribe(phase = Subscribe.Phase.POST)
  public void readEventConfigsPost(Event event) {
    this.readEventConfigs(event, Subscribe.Phase.POST);
  }

  public void readEventConfigs(Event event, Subscribe.Phase phase) {
    Class<?> eventClass = event.getClass();

    if (!this.pendingConfigInitializations.isEmpty()) {
      while (!eventClass.equals(Object.class)) {
        if (this.pendingConfigInitializations.containsKey(eventClass)) {
          this.pendingConfigInitializations
              .get(eventClass)
              .removeIf(
                  pendingInitialization -> {
                    Subscribe.Phase targetPhase = pendingInitialization.getFirst();
                    ParsedConfig targetConfig = pendingInitialization.getSecond();

                    if (targetPhase.equals(phase)) {
                      this.configGenerator.initConfig(targetConfig);
                      return true;
                    }

                    return false;
                  });
          break;
        }

        eventClass = eventClass.getSuperclass();
      }
    }
  }
}
