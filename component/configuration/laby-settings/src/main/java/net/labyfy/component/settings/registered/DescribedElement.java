package net.labyfy.component.settings.registered;

import net.labyfy.chat.component.ChatComponent;

public interface DescribedElement {

  // non-null
  ChatComponent getDisplayName();

  // nullable
  ChatComponent getDescription();

}
