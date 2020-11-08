package net.flintmc.mcapi.internal.chat.annotation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.builder.TextComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;

@Singleton
@Implement(ComponentAnnotationSerializer.class)
public class DefaultComponentAnnotationSerializer implements ComponentAnnotationSerializer {

  private final ComponentBuilder.Factory builderFactory;
  private final ComponentSerializer.Factory serializerFactory;

  @Inject
  private DefaultComponentAnnotationSerializer(ComponentBuilder.Factory builderFactory,
                                               ComponentSerializer.Factory serializerFactory) {
    this.builderFactory = builderFactory;
    this.serializerFactory = serializerFactory;
  }

  @Override
  public ChatComponent deserialize(Component component) {
    if (component.translate()) {
      // the value will be used as a translation key
      return this.builderFactory.translation().translationKey(component.value()).build();
    }

    // the value will be parsed as a legacy text like "§ctest"
    return this.serializerFactory.legacy().deserialize(component.value());
  }

  @Override
  public ChatComponent deserialize(Component[] components) {
    TextComponentBuilder builder = this.builderFactory.text().text("");

    for (Component component : components) {
      builder.append(this.deserialize(component));
    }

    return builder.build();
  }

  @Override
  public ChatComponent deserialize(Component[] components, ChatComponent def) {
    if (components.length == 0) {
      return def;
    }

    return this.deserialize(components);
  }

  @Override
  public ChatComponent deserialize(Component[] components, String def) {
    if (components.length == 0) {
      return this.serializerFactory.legacy().deserialize(def);
    }

    return this.deserialize(components);
  }
}
