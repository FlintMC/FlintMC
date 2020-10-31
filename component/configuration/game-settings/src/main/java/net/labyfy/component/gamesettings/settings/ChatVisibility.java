package net.labyfy.component.gamesettings.settings;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DisplayName;

/**
 * An enumeration representing chat visibility states.
 */
public enum ChatVisibility {

  // TODO use the displayName from enums


  @DisplayName(@Component(value = "options.chat.visibility.full", translate = true))
  FULL("options.chat.visibility.full"),

  @DisplayName(@Component(value = "options.chat.visibility.system", translate = true))
  SYSTEM("options.chat.visibility.system"),

  @DisplayName(@Component(value = "options.chat.visibility.hidden", translate = true))
  HIDDEN("options.chat.visibility.hidden");

  private final String resourceKey;

  ChatVisibility(String resourceKey) {
    this.resourceKey = resourceKey;
  }

  public String getResourceKey() {
    return resourceKey;
  }
}
