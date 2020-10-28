package net.labyfy.component.settings.registered;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.inject.assisted.AssistedFactory;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

public interface RegisteredSetting extends DescribedElement {

  ParsedConfig getConfig();

  Annotation getAnnotation();

  Object getCurrentValue();

  // value nullable, if null will be replaced with the default value
  boolean setCurrentValue(Object value);

  String getCategoryName();

  @AssistedFactory(RegisteredSetting.class)
  interface Factory {

    RegisteredSetting create(@Assisted("displayName") ChatComponent displayName,
                             @Assisted("description") @Nullable ChatComponent description,
                             @Assisted Annotation annotation,
                             @Assisted ParsedConfig config,
                             @Assisted @Nullable String categoryName,
                             @Assisted ConfigObjectReference reference);

  }

}
