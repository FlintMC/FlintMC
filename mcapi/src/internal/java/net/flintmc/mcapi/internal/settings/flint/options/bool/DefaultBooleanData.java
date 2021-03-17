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

package net.flintmc.mcapi.internal.settings.flint.options.bool;

import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.settings.flint.options.bool.BooleanData;
import net.flintmc.mcapi.settings.flint.options.data.SettingDataUpdateEvent;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import javax.annotation.Nullable;

@Implement(BooleanData.class)
public class DefaultBooleanData implements BooleanData {

  private final EventBus eventBus;
  private final SettingDataUpdateEvent updateEvent;
  private final RegisteredSetting setting;

  private ChatComponent enabledText;
  private ChatComponent disabledText;

  @AssistedInject
  private DefaultBooleanData(
      EventBus eventBus,
      SettingDataUpdateEvent.Factory updateEventFactory,
      @Assisted("setting") RegisteredSetting setting,
      @Assisted("enabledText") @Nullable ChatComponent enabledText,
      @Assisted("disabledText") @Nullable ChatComponent disabledText) {
    this.eventBus = eventBus;
    this.updateEvent = updateEventFactory.create(this);
    this.setting = setting;
    this.enabledText = enabledText;
    this.disabledText = disabledText;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RegisteredSetting getSetting() {
    return this.setting;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getEnabledText() {
    return this.enabledText;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEnabledText(ChatComponent enabledText) {
    this.eventBus.fireEventAll(this.updateEvent, () -> this.enabledText = enabledText);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDisabledText() {
    return this.disabledText;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDisabledText(ChatComponent disabledText) {
    this.eventBus.fireEventAll(this.updateEvent, () -> this.disabledText = disabledText);
  }
}
