package net.flintmc.framework.config.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.EventConfigInitializer;
import net.flintmc.framework.config.annotation.ConfigInit;
import net.flintmc.framework.config.generator.ConfigGenerator;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.method.SubscribeMethodBuilder;
import net.flintmc.framework.inject.implement.Implement;

/** {@inheritDoc} */
@Singleton
@Implement(EventConfigInitializer.class)
public class DefaultEventConfigInitializer implements EventConfigInitializer {

  private final EventBus eventBus;
  private final SubscribeMethodBuilder.Factory subscribeMethodFactory;
  private final ConfigGenerator configGenerator;

  @Inject
  public DefaultEventConfigInitializer(
      SubscribeMethodBuilder.Factory subscribeMethodFactory,
      EventBus eventBus,
      ConfigGenerator configGenerator) {
    this.eventBus = eventBus;
    this.subscribeMethodFactory = subscribeMethodFactory;
    this.configGenerator = configGenerator;
  }

  /** {@inheritDoc} */
  @Override
  public void registerPendingInitialization(ParsedConfig config, ConfigInit configInit) {
    this.subscribeMethodFactory
        .newBuilder(configInit.value())
        .phaseOnly(configInit.eventPhase())
        .to(
            ((event, phase, holderMethod) -> {
              this.configGenerator.initConfig(config);
              // we only needed the subscribe method to initialize the config,
              // we can unregister it now
              this.eventBus.unregisterSubscribeMethod(holderMethod);
            }))
        .buildAndRegister();
  }
}
