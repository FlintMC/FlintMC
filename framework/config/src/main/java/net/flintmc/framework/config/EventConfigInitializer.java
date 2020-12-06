package net.flintmc.framework.config;

import net.flintmc.framework.config.annotation.ConfigInit;
import net.flintmc.framework.config.generator.ParsedConfig;

public interface EventConfigInitializer {

  void addPendingInitialization(ParsedConfig config, ConfigInit configInit);
}
