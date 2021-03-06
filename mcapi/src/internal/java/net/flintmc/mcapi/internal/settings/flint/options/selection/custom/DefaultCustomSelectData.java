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

package net.flintmc.mcapi.internal.settings.flint.options.selection.custom;

import com.google.inject.Inject;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.settings.flint.options.data.SettingDataUpdateEvent;
import net.flintmc.mcapi.settings.flint.options.selection.SelectMenuType;
import net.flintmc.mcapi.settings.flint.options.selection.SelectionEntry;
import net.flintmc.mcapi.settings.flint.options.selection.custom.CustomSelectData;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import java.util.ArrayList;
import java.util.Collection;

@Implement(CustomSelectData.class)
public class DefaultCustomSelectData implements CustomSelectData {

  private final EventBus eventBus;
  private final SettingDataUpdateEvent updateEvent;
  private final RegisteredSetting setting;

  private final Collection<SelectionEntry> entries;

  private SelectMenuType type;

  @Inject
  private DefaultCustomSelectData(
      EventBus eventBus,
      SettingDataUpdateEvent.Factory updateEventFactory,
      @Assisted("setting") RegisteredSetting setting,
      @Assisted("type") SelectMenuType type,
      @Assisted("entries") Collection<SelectionEntry> entries) {
    this.eventBus = eventBus;
    this.updateEvent = updateEventFactory.create(this);
    this.setting = setting;
    this.type = type;
    this.entries = new ArrayList<>(entries);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addEntry(SelectionEntry entry) {
    if (this.entries.contains(entry)) {
      return;
    }

    this.eventBus.fireEventAll(this.updateEvent, () -> this.entries.add(entry));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeEntry(SelectionEntry entry) {
    if (!this.entries.contains(entry)) {
      return;
    }

    this.eventBus.fireEventAll(this.updateEvent, () -> this.entries.remove(entry));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SelectMenuType getType() {
    return this.type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setType(SelectMenuType type) {
    if (this.type == type) {
      return;
    }

    this.eventBus.fireEventAll(this.updateEvent, () -> this.type = type);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<SelectionEntry> getEntries() {
    return this.entries;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RegisteredSetting getSetting() {
    return this.setting;
  }
}
