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

package net.flintmc.mcapi.v1_16_5.settings.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.storage.ConfigStorageProvider;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.mcapi.settings.game.MinecraftConfiguration;
import net.flintmc.mcapi.settings.game.event.GameSettingsStorageEvent;
import net.flintmc.mcapi.settings.game.event.GameSettingsStorageEvent.State;
import net.flintmc.transform.hook.Hook;

@Singleton
public class VersionedGameSettingsInterceptor {

  private final ConfigStorageProvider storageProvider;
  private final MinecraftConfiguration configuration;
  private final EventBus eventBus;
  private final GameSettingsStorageEvent loadEvent;
  private final GameSettingsStorageEvent saveEvent;

  @Inject
  private VersionedGameSettingsInterceptor(
      ConfigStorageProvider storageProvider,
      MinecraftConfiguration configuration,
      EventBus eventBus,
      GameSettingsStorageEvent.Factory eventFactory) {
    this.storageProvider = storageProvider;
    this.configuration = configuration;
    this.eventBus = eventBus;
    this.loadEvent = eventFactory.create(State.LOAD);
    this.saveEvent = eventFactory.create(State.SAVE);
  }

  @Hook(
      className = "net.minecraft.client.GameSettings",
      methodName = "loadOptions",
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER})
  public void hookLoadOptions(Hook.ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.loadEvent, executionTime);
  }

  @Hook(
      className = "net.minecraft.client.GameSettings",
      methodName = "saveOptions",
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER})
  public void hookSaveOptions(Hook.ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.saveEvent, executionTime);

    if (executionTime == Hook.ExecutionTime.AFTER) {
      // write the Flint config when an option is changed in the vanilla minecraft options screen
      this.storageProvider.write((ParsedConfig) this.configuration);
    }
  }
}
