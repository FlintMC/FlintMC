package net.labyfy.component.settings.registered;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.inject.assisted.AssistedFactory;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

public interface RegisteredSetting {

  ParsedConfig getConfig();

  ConfigObjectReference getReference();

  Annotation getAnnotation();

  // if null, the defaultValue will be used
  Object getCurrentValue();

  // value nullable, if null will be replaced with the default value
  boolean setCurrentValue(Object value);

  String getCategoryName();

  boolean isNative();

  boolean isEnabled();

  void setEnabled(boolean enabled);

  @AssistedFactory(RegisteredSetting.class)
  interface Factory {

    RegisteredSetting create(@Assisted Class<? extends Annotation> annotationType,
                             @Assisted ParsedConfig config,
                             @Assisted @Nullable String categoryName,
                             @Assisted ConfigObjectReference reference);

  }

}
