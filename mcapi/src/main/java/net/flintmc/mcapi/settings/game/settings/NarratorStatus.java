package net.flintmc.mcapi.settings.game.settings;

import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;

/**
 * An enumeration representing narrator statuses.
 */
public enum NarratorStatus {

  @DisplayName(@Component(value = "options.narrator.off", translate = true))
  OFF(),

  @DisplayName(@Component(value = "options.narrator.all", translate = true))
  ALL(),

  @DisplayName(@Component(value = "options.narrator.chat", translate = true))
  CHAT(),

  @DisplayName(@Component(value = "options.narrator.system", translate = true))
  SYSTEM()

}
