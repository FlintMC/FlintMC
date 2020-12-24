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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.settings.flint.event.SettingUpdateEvent;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;

@Implement(RegisteredSetting.class)
public class DefaultRegisteredSetting implements RegisteredSetting {

  private final SettingHandler settingHandler;
  private final Class<? extends Annotation> annotationType;
  private final String categoryName;
  private final ConfigObjectReference reference;

  private final Collection<RegisteredSetting> subSettings;

  private final EventBus eventBus;
  private final SettingUpdateEvent updateEvent;
  private boolean enabled;

  @AssistedInject
  public DefaultRegisteredSetting(
      SettingHandler settingHandler,
      EventBus eventBus,
      SettingUpdateEvent.Factory eventFactory,
      @Assisted Class<? extends Annotation> annotationType,
      @Assisted @Nullable String categoryName,
      @Assisted ConfigObjectReference reference) {
    this.settingHandler = settingHandler;
    this.annotationType = annotationType;
    this.categoryName = categoryName;
    this.reference = reference;

    this.subSettings = new HashSet<>();

    this.enabled = true;
    this.eventBus = eventBus;
    this.updateEvent = eventFactory.create(this);
  }

  @Override
  public ConfigObjectReference getReference() {
    return this.reference;
  }

  @Override
  public Annotation getAnnotation() {
    return this.reference.findLastAnnotation(this.annotationType);
  }

  @Override
  public Object getCurrentValue() {
    return this.reference.getValue();
  }

  @Override
  public boolean setCurrentValue(Object value) {
    if (!this.settingHandler.isValidInput(value, this.reference, this.getAnnotation())) {
      return false;
    }

    this.reference.setValue(value);
    return true;
  }

  @Override
  public String getCategoryName() {
    return this.categoryName;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    if (this.enabled == enabled) {
      return;
    }

    this.eventBus.fireEvent(this.updateEvent, Subscribe.Phase.PRE);
    this.enabled = enabled;
    this.eventBus.fireEvent(this.updateEvent, Subscribe.Phase.POST);
  }

  @Override
  public Collection<RegisteredSetting> getSubSettings() {
    return this.subSettings;
  }

  @Override
  public String toString() {
    return "DefaultRegisteredSetting(" + this.reference + ")";
  }
}
