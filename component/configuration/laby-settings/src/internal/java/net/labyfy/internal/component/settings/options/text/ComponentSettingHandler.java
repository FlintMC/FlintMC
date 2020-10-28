package net.labyfy.internal.component.settings.options.text;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.component.TextComponent;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.text.ComponentSetting;

@Singleton
@RegisterSettingHandler(ComponentSetting.class)
public class ComponentSettingHandler implements SettingHandler<ComponentSetting> {

  private final ComponentSerializer.Factory serializerFactory;

  @Inject
  public ComponentSettingHandler(ComponentSerializer.Factory serializerFactory) {
    this.serializerFactory = serializerFactory;
  }

  @Override
  public Object getDefaultValue(ComponentSetting annotation, ConfigObjectReference reference) {
    return this.serializerFactory.legacy().deserialize(annotation.defaultValue());
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, ComponentSetting annotation) {
    return input instanceof TextComponent;
  }
}
