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
import net.flintmc.framework.config.SingletonConfigHolder;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.inject.implement.Implement;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Implement(SingletonConfigHolder.class)
public class DefaultSingletonConfigHolder implements SingletonConfigHolder {

  private final Map<Class<? extends ParsedConfig>, ParsedConfig> singletonConfigs;

  @Inject
  private DefaultSingletonConfigHolder() {
    this.singletonConfigs = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ParsedConfig getSingletonConfig(Class<? extends ParsedConfig> configClass) {
    return this.singletonConfigs.get(configClass);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<ParsedConfig> getSingletonConfigs() {
    return Collections.unmodifiableCollection(this.singletonConfigs.values());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerSingletonConfig(ParsedConfig config) {
    Class<? extends ParsedConfig> configClass =
        (Class<? extends ParsedConfig>) config.getConfigClass();

    if (this.singletonConfigs.containsKey(configClass)) {
      throw new IllegalStateException(
          "A config with the class " + configClass.getName()
              + " is already registered as a singleton");
    }

    this.singletonConfigs.put(configClass, config);
  }
}
