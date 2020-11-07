package net.labyfy.internal.component.settings.registered;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.settings.annotation.ui.NativeSetting;
import net.labyfy.component.settings.event.SettingsUpdateEvent;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.registered.RegisteredSetting;

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

  private final boolean nativeSetting;
  private final EventBus eventBus;
  private final SettingsUpdateEvent updateEvent;
  private boolean enabled;

  @AssistedInject
  public DefaultRegisteredSetting(SettingHandler settingHandler,
                                  EventBus eventBus,
                                  SettingsUpdateEvent.Factory eventFactory,
                                  @Assisted Class<? extends Annotation> annotationType,
                                  @Assisted @Nullable String categoryName,
                                  @Assisted ConfigObjectReference reference) {
    this.settingHandler = settingHandler;
    this.annotationType = annotationType;
    this.categoryName = categoryName;
    this.reference = reference;

    this.subSettings = new HashSet<>();

    this.nativeSetting = reference.findLastAnnotation(NativeSetting.class) != null;

    this.enabled = true;
    this.eventBus = eventBus;
    this.updateEvent = eventFactory.create(this);
  }

  @Override
  public ParsedConfig getConfig() {
    return this.reference.getConfig();
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
  public boolean isNative() {
    return this.nativeSetting;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
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
