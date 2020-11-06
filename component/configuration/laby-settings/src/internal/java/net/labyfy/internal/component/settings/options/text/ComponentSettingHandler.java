package net.labyfy.internal.component.settings.options.text;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.TextComponent;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.text.ComponentSetting;
import net.labyfy.component.settings.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(ComponentSetting.class)
public class ComponentSettingHandler implements SettingHandler<ComponentSetting> {

  private final ComponentSerializer.Factory serializerFactory;
  private final ChatComponent emptyComponent;

  @Inject
  public ComponentSettingHandler(ComponentSerializer.Factory serializerFactory, ComponentBuilder.Factory builderFactory) {
    this.serializerFactory = serializerFactory;
    this.emptyComponent = builderFactory.text().text("").build();
  }

  @Override
  public JsonObject serialize(ComponentSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    object.add("value", this.serializerFactory.gson().getGson()
        .toJsonTree(currentValue != null ? currentValue : this.emptyComponent));

    return object;
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, ComponentSetting annotation) {
    return input instanceof TextComponent;
  }
}
