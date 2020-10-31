package net.labyfy.chat.annotation;

import net.labyfy.chat.component.ChatComponent;

public interface ComponentAnnotationSerializer {

  ChatComponent deserialize(Component component);

  ChatComponent deserialize(Component[] components);

}
