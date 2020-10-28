package net.labyfy.internal.component.settings.registered;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.registered.RegisteredSetting;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

@Implement(RegisteredSetting.class)
public class DefaultRegisteredSetting implements RegisteredSetting {

  private final Logger logger;
  private final SettingHandler settingHandler;
  private final ChatComponent displayName;
  private final ChatComponent description;
  private final Annotation annotation;
  private final Object defaultValue;
  private final ParsedConfig config;
  private final String categoryName;
  private final ConfigObjectReference reference;

  @AssistedInject
  public DefaultRegisteredSetting(@InjectLogger Logger logger,
                                  SettingHandler settingHandler,
                                  @Assisted("displayName") ChatComponent displayName,
                                  @Assisted("description") @Nullable ChatComponent description,
                                  @Assisted Annotation annotation,
                                  @Assisted ParsedConfig config,
                                  @Assisted @Nullable String categoryName,
                                  @Assisted ConfigObjectReference reference) throws InvocationTargetException, IllegalAccessException {
    this.logger = logger;
    this.settingHandler = settingHandler;
    this.displayName = displayName;
    this.description = description;
    this.annotation = annotation;
    this.config = config;
    this.categoryName = categoryName;
    this.reference = reference;
    this.defaultValue = settingHandler.getDefaultValue(annotation, reference);

    this.reference.setValue(this.config, this.defaultValue);
  }

  @Override
  public ParsedConfig getConfig() {
    return this.config;
  }

  @Override
  public Annotation getAnnotation() {
    return this.annotation;
  }

  @Override
  public Object getCurrentValue() {
    try {
      Object value = this.reference.getValue(this.config);
      if (value != null) {
        return value;
      }
    } catch (InvocationTargetException | IllegalAccessException e) {
      this.logger.error("Failed to get the value from " + this.reference.getKey()
          + " for setting from config " + this.config.getConfigName(), e);
    }

    return this.defaultValue;
  }

  @Override
  public boolean setCurrentValue(Object value) {
    if (!this.settingHandler.isValidInput(value, this.reference, this.annotation)) {
      return false;
    }

    try {
      this.reference.setValue(this.config, value != null ? value : this.defaultValue);

      return true;
    } catch (InvocationTargetException | IllegalAccessException e) {
      this.logger.error("Failed to update the value of " + this.reference.getKey()
          + " for setting from config " + this.config.getConfigName(), e);

      return false;
    }
  }

  @Override
  public String getCategoryName() {
    return this.categoryName;
  }

  @Override
  public ChatComponent getDisplayName() {
    return this.displayName;
  }

  @Override
  public ChatComponent getDescription() {
    return this.description;
  }
}
