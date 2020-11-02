package net.labyfy.component.gamesettings.settings;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DisplayName;

/**
 * An enumeration representing chat visibility states.
 */
public enum ChatVisibility {

  @DisplayName(@Component(value = "options.chat.visibility.full", translate = true))
  FULL(),

  @DisplayName(@Component(value = "options.chat.visibility.system", translate = true))
  SYSTEM(),

  @DisplayName(@Component(value = "options.chat.visibility.hidden", translate = true))
  HIDDEN()

}
