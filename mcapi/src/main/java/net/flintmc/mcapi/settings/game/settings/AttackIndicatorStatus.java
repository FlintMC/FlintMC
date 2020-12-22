package net.flintmc.mcapi.settings.game.settings;

import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;

/**
 * An enumeration representing attack indicator statuses.
 */
public enum AttackIndicatorStatus {
  @DisplayName(@Component(value = "options.off", translate = true))
  OFF(),

  @DisplayName(@Component(value = "options.attack.crosshair", translate = true))
  CROSSHAIR(),

  @DisplayName(@Component(value = "options.attack.hotbar", translate = true))
  HOTBAR()
}
