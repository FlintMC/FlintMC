package net.labyfy.component.gamesettings.settings;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DisplayName;

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
