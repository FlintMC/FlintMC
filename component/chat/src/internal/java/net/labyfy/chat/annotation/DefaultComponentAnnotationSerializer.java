package net.labyfy.chat.annotation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.builder.TextComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.component.inject.implement.Implement;

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
}
