package net.flintmc.mcapi.settings.game.settings;

import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;

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
