package net.flintmc.mcapi.settings.game.settings;

import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;

/**
 * An enumeration representing particle statuses.
 */
public enum ParticleStatus {
  @DisplayName(@Component(value = "options.particles.all", translate = true))
  ALL(),

  @DisplayName(@Component(value = "options.particles.decreased", translate = true))
  DECREASED(),

  @DisplayName(@Component(value = "options.particles.minimal", translate = true))
  MINIMAL()
}
