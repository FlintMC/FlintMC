package net.flintmc.mcapi.settings.game.settings;

import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;

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
