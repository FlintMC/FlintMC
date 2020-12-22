package net.flintmc.mcapi.settings.game.settings;

import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;

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
