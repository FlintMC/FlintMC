package net.flintmc.mcapi.internal.settings.flint.options.text;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.text.ComponentSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

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
