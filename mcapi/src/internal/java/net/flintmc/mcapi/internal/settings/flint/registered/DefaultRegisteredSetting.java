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

import com.google.inject.name.Named;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.annotation.ui.Description;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.annotation.ui.ForceFullWidth;
import net.flintmc.mcapi.settings.flint.annotation.version.VersionExclude;
import net.flintmc.mcapi.settings.flint.annotation.version.VersionOnly;
import net.flintmc.mcapi.settings.flint.event.SettingUpdateEvent;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

@Implement(RegisteredSetting.class)
public class DefaultRegisteredSetting implements RegisteredSetting {

  private final SettingHandler settingHandler;
  private final ComponentAnnotationSerializer annotationSerializer;

  private final String categoryName;
  private final ConfigObjectReference reference;

  private final Annotation annotation;
  private final String type;
  private final SettingData data;

  private final Collection<RegisteredSetting> subSettings;

  private final EventBus eventBus;
  private final SettingUpdateEvent updateEvent;
  private boolean enabled;

  private final String gameVersion;

  @AssistedInject
  public DefaultRegisteredSetting(
      SettingHandler settingHandler,
      ComponentAnnotationSerializer annotationSerializer,
      EventBus eventBus,
      SettingUpdateEvent.Factory eventFactory,
      @Named("launchArguments") Map launchArguments,
      @Assisted Class<? extends Annotation> annotationType,
      @Assisted @Nullable String categoryName,
      @Assisted ConfigObjectReference reference) {
    this.settingHandler = settingHandler;
    this.annotationSerializer = annotationSerializer;
    this.categoryName = categoryName;
    this.reference = reference;

    this.annotation = this.reference.findLastAnnotation(annotationType);

    ApplicableSetting applicableSetting =
        this.annotation.annotationType().getAnnotation(ApplicableSetting.class);
    this.type = applicableSetting.name();
    this.data = applicableSetting.data() == ApplicableSetting.DummySettingData.class ? null
        : settingHandler.createData(this.annotation, this);

    this.subSettings = new HashSet<>();

    this.gameVersion = (String) launchArguments.get("--game-version");

    this.enabled = true;
    this.eventBus = eventBus;
    this.updateEvent = eventFactory.create(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ConfigObjectReference getReference() {
    return this.reference;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getType() {
    return this.type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Annotation getAnnotation() {
    return this.annotation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public <T extends SettingData> T getData() {
    return (T) this.data;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCurrentValue() {
    return this.reference.getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean setCurrentValue(Object value) {
    if (!this.settingHandler.isValidInput(value, this.reference, this.getAnnotation())) {
      return false;
    }

    this.reference.setValue(value);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCategoryName() {
    return this.categoryName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEnabled(boolean enabled) {
    if (this.enabled == enabled) {
      return;
    }

    this.eventBus.fireEvent(this.updateEvent, Subscribe.Phase.PRE);
    this.enabled = enabled;
    this.eventBus.fireEvent(this.updateEvent, Subscribe.Phase.POST);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDisplayName(Object key) {
    DisplayName name = this.reference.findLastAnnotation(DisplayName.class, key);
    return name == null ? null : this.annotationSerializer.deserialize(name.value());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDescription(Object key) {
    Description description = this.reference.findLastAnnotation(Description.class, key);
    return description == null ? null : this.annotationSerializer.deserialize(description.value());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isFullWidthForced(Object key) {
    return this.reference.findLastAnnotation(ForceFullWidth.class, key) != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHidden(Object key) {
    VersionOnly only = reference.findLastAnnotation(VersionOnly.class, key);
    if (only != null && Arrays.asList(only.value()).contains(this.gameVersion)) {
      return false;
    }

    VersionExclude exclude = reference.findLastAnnotation(VersionExclude.class, key);
    return exclude != null && Arrays.asList(exclude.value()).contains(this.gameVersion);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<RegisteredSetting> getSubSettings() {
    return this.subSettings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "DefaultRegisteredSetting(" + this.reference + ")";
  }
}
