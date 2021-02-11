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
        .to(((event, phase, holderMethod) -> {
          this.configGenerator.initConfig(config);
          // we only needed the subscribe method to initialize the config,
          // we can unregister it now
          this.eventBus.unregisterSubscribeMethod(holderMethod);
        }))
        .buildAndRegister();
  }
}
