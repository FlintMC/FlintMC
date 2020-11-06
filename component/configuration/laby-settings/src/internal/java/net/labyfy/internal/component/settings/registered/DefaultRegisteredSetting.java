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

@Implement(RegisteredSetting.class)
public class DefaultRegisteredSetting implements RegisteredSetting {

  private final SettingHandler settingHandler;
  private final Class<? extends Annotation> annotationType;
  private final ParsedConfig config;
  private final String categoryName;
  private final ConfigObjectReference reference;

  private final boolean nativeSetting;
  private final EventBus eventBus;
  private final SettingsUpdateEvent updateEvent;
  private boolean enabled;

  @AssistedInject
  public DefaultRegisteredSetting(SettingHandler settingHandler,
                                  EventBus eventBus,
                                  SettingsUpdateEvent.Factory eventFactory,
                                  @Assisted Class<? extends Annotation> annotationType,
                                  @Assisted ParsedConfig config,
                                  @Assisted @Nullable String categoryName,
                                  @Assisted ConfigObjectReference reference) {
    this.settingHandler = settingHandler;
    this.annotationType = annotationType;
    this.config = config;
    this.categoryName = categoryName;
    this.reference = reference;

    this.nativeSetting = reference.findLastAnnotation(NativeSetting.class) != null;

    this.enabled = true;
    this.eventBus = eventBus;
    this.updateEvent = eventFactory.create(this);
  }

  @Override
  public ParsedConfig getConfig() {
    return this.config;
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
    return this.reference.getValue(this.config);
  }

  @Override
  public boolean setCurrentValue(Object value) {
    if (!this.settingHandler.isValidInput(value, this.reference, this.getAnnotation())) {
      return false;
    }

    this.reference.setValue(this.config, value);
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

}
