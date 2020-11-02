package net.labyfy.component.gamesettings.settings;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DisplayName;

/**
 * An enumeration representing ambient occlusion statuses.
 */
public enum AmbientOcclusionStatus {

  @DisplayName(@Component(value = "options.ao.off", translate = true))
  OFF(),

  @DisplayName(@Component(value = "options.ao.min", translate = true))
  MIN(),

  @DisplayName(@Component(value = "options.ao.max", translate = true))
  MAX()

}
