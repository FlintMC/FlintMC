package net.labyfy.component.gamesettings.settings;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DisplayName;

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
