package net.labyfy.component.gamesettings.settings;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DisplayName;

/**
 * An enumeration representing options for the graphics.
 */
public enum GraphicsFanciness {

  @DisplayName(@Component(value = "options.graphics.fast", translate = true))
  FAST(),

  @DisplayName(@Component(value = "options.graphics.fancy", translate = true))
  FANCY(),

  @DisplayName(@Component(value = "options.graphics.fabulous", translate = true))
  FABULOUS()

}
