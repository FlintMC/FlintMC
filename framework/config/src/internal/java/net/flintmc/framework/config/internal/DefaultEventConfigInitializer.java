package net.flintmc.framework.config.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.EventConfigInitializer;
import net.flintmc.framework.config.annotation.ConfigInit;
import net.flintmc.framework.config.generator.ConfigGenerator;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.eventbus.method.SubscribeMethodBuilder;
import net.flintmc.framework.inject.implement.Implement;

/** {@inheritDoc} */
@Singleton
@Implement(EventConfigInitializer.class)
public class DefaultEventConfigInitializer implements EventConfigInitializer {

  private final ConfigGenerator configGenerator;
  private final SubscribeMethodBuilder.Factory subscribeMethodFactory;

  @Inject
  public DefaultEventConfigInitializer(
      ConfigGenerator configGenerator, SubscribeMethodBuilder.Factory subscribeMethodFactory) {
    this.configGenerator = configGenerator;
    this.subscribeMethodFactory = subscribeMethodFactory;
  }

  /** {@inheritDoc} */
  @Override
  public void registerPendingInitialization(ParsedConfig config, ConfigInit configInit) {
    this.subscribeMethodFactory
        .newBuilder(configInit.eventClass())
        .phaseOnly(configInit.eventPhase())
        .to(ignored -> this.configGenerator.initConfig(config))
        .buildAndRegister();
  }
}
