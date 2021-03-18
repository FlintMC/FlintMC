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

package net.flintmc.mcapi.internal.settings.flint.options.text;

import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.settings.flint.options.data.SettingDataUpdateEvent;
import net.flintmc.mcapi.settings.flint.options.text.string.StringData;
import net.flintmc.mcapi.settings.flint.options.text.string.StringRestriction;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Implement(StringData.class)
public class DefaultStringData implements StringData {

  private final EventBus eventBus;
  private final SettingDataUpdateEvent updateEvent;
  private final RegisteredSetting setting;

  private final Collection<StringRestriction> restrictions;

  private int maxLength;
  private String prefix;
  private String suffix;
  private String placeholder;

  @AssistedInject
  private DefaultStringData(
      EventBus eventBus,
      SettingDataUpdateEvent.Factory updateEventFactory,
      @Assisted("setting") RegisteredSetting setting) {
    this(eventBus, updateEventFactory, setting, "", "", "", 0, Collections.emptyList());
  }

  @AssistedInject
  private DefaultStringData(
      EventBus eventBus,
      SettingDataUpdateEvent.Factory updateEventFactory,
      @Assisted("setting") RegisteredSetting setting,
      @Assisted("prefix") String prefix,
      @Assisted("suffix") String suffix,
      @Assisted("placeholder") String placeholder,
      @Assisted("maxLength") int maxLength,
      @Assisted("restrictions") Collection<StringRestriction> restrictions) {
    this.eventBus = eventBus;
    this.updateEvent = updateEventFactory.create(this);
    this.setting = setting;

    this.maxLength = maxLength;
    this.prefix = prefix;
    this.suffix = suffix;
    this.placeholder = placeholder;
    this.restrictions = new ArrayList<>(restrictions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxLength() {
    return this.maxLength;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMaxLength(int maxLength) {
    if (this.maxLength == maxLength) {
      return;
    }

    this.eventBus.fireEventAll(this.updateEvent, () -> this.maxLength = Math.max(maxLength, 0));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPrefix() {
    return this.prefix;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPrefix(String prefix) {
    if (this.prefix.equals(prefix)) {
      return;
    }

    this.eventBus.fireEventAll(this.updateEvent, () -> this.prefix = prefix != null ? prefix : "");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSuffix() {
    return this.suffix;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSuffix(String suffix) {
    if (this.suffix.equals(suffix)) {
      return;
    }

    this.eventBus.fireEventAll(this.updateEvent, () -> this.suffix = suffix != null ? suffix : "");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPlaceholder() {
    return this.placeholder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPlaceholder(String placeholder) {
    if (this.placeholder.equals(placeholder)) {
      return;
    }

    this.eventBus.fireEventAll(
        this.updateEvent, () -> this.placeholder = placeholder != null ? placeholder : "");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<StringRestriction> getRestrictions() {
    return this.restrictions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addRestriction(StringRestriction restriction) {
    if (this.restrictions.contains(restriction)) {
      return;
    }

    this.eventBus.fireEventAll(this.updateEvent, () -> this.restrictions.add(restriction));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeRestriction(StringRestriction restriction) {
    if (!this.restrictions.contains(restriction)) {
      return;
    }

    this.eventBus.fireEventAll(this.updateEvent, () -> this.restrictions.remove(restriction));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RegisteredSetting getSetting() {
    return this.setting;
  }
}
