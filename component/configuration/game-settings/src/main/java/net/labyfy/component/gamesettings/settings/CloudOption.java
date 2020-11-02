package net.labyfy.component.gamesettings.settings;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DisplayName;

/**
 * An enumeration representing options for clouds.
 */
public enum CloudOption {

  @DisplayName(@Component(value = "options.off", translate = true))
  OFF(),

  @DisplayName(@Component(value = "options.clouds.fast", translate = true))
  FAST(),

  @DisplayName(@Component(value = "options.clouds.fancy", translate = true))
  FANCY()

}
