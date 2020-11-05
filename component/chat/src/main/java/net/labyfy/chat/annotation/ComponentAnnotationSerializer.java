package net.labyfy.chat.annotation;

import net.labyfy.chat.component.ChatComponent;

public interface ComponentAnnotationSerializer {

  ChatComponent deserialize(Component component);

  ChatComponent deserialize(Component[] components);

  // def used if components.length == 0
  ChatComponent deserialize(Component[] components, ChatComponent def);

  // def used as legacy text if components.length == 0
  ChatComponent deserialize(Component[] components, String def);

}
