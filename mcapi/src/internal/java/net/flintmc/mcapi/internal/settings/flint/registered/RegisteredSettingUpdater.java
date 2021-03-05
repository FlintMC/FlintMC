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

package net.flintmc.mcapi.internal.settings.flint.registered;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.event.ConfigValueUpdateEvent;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.settings.flint.event.SettingUpdateEvent;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.registered.SettingsProvider;

@Singleton
public class RegisteredSettingUpdater {

  private final SettingsProvider provider;
  private final EventBus eventBus;
  private final SettingUpdateEvent.Factory eventFactory;

  @Inject
  private RegisteredSettingUpdater(
      SettingsProvider provider, EventBus eventBus, SettingUpdateEvent.Factory eventFactory) {
    this.provider = provider;
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
  }

  @Subscribe(Subscribe.Phase.PRE)
  public void preUpdateSettings(ConfigValueUpdateEvent event) {
    this.fireEvent(event.getReference(), Subscribe.Phase.PRE);
  }

  @Subscribe(Subscribe.Phase.POST)
  public void postUpdateSettings(ConfigValueUpdateEvent event) {
    this.fireEvent(event.getReference(), Subscribe.Phase.POST);
  }

  private void fireEvent(ConfigObjectReference reference, Subscribe.Phase phase) {
    RegisteredSetting setting = this.provider.getSetting(reference);
    if (setting == null) {
      return;
    }

    SettingUpdateEvent fired = this.eventFactory.create(setting);
    this.eventBus.fireEvent(fired, phase);
  }
}
