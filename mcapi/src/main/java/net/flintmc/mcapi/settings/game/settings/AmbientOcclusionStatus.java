package net.flintmc.mcapi.settings.game.settings;

import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;

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
