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

package net.flintmc.mcapi.internal.settings.flint.options.numeric;

import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.settings.flint.options.data.SettingDataUpdateEvent;
import net.flintmc.mcapi.settings.flint.options.numeric.NumericData;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import java.util.Map;

@Implement(NumericData.class)
public class DefaultNumericData implements NumericData {

  private final EventBus eventBus;
  private final SettingDataUpdateEvent updateEvent;
  private final RegisteredSetting setting;

  private final Map<Double, ChatComponent> overriddenDisplays;

  private double minValue;
  private double maxValue;
  private int decimalPlaces;

  @AssistedInject
  private DefaultNumericData(
      EventBus eventBus,
      SettingDataUpdateEvent.Factory updateEventFactory,
      @Assisted("setting") RegisteredSetting setting,
      @Assisted("minValue") double minValue,
      @Assisted("maxValue") double maxValue,
      @Assisted("decimalPlaces") int decimalPlaces,
      @Assisted("overriddenDisplays") Map<Double, ChatComponent> overriddenDisplays) {
    this.eventBus = eventBus;
    this.updateEvent = updateEventFactory.create(this);
    this.setting = setting;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.decimalPlaces = decimalPlaces;
    this.overriddenDisplays = overriddenDisplays;
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
  public double getMinValue() {
    return this.minValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMinValue(double minValue) {
    if (this.minValue == minValue) {
      return;
    }

    this.eventBus.fireEventAll(this.updateEvent, () -> this.minValue = minValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getMaxValue() {
    return this.maxValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMaxValue(double maxValue) {
    if (this.maxValue == maxValue) {
      return;
    }

    this.eventBus.fireEventAll(this.updateEvent, () -> this.maxValue = maxValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getDecimalPlaces() {
    return this.decimalPlaces;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDecimalPlaces(int decimalPlaces) {
    if (this.decimalPlaces == decimalPlaces) {
      return;
    }

    this.eventBus.fireEventAll(this.updateEvent, () -> this.decimalPlaces = decimalPlaces);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getOverriddenDisplay(double value) {
    return this.overriddenDisplays.get(value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOverriddenDisplay(double value, ChatComponent display) {
    this.eventBus.fireEventAll(this.updateEvent, () -> this.overriddenDisplays.put(value, display));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<Double, ChatComponent> getOverriddenDisplays() {
    return this.overriddenDisplays;
  }
}
