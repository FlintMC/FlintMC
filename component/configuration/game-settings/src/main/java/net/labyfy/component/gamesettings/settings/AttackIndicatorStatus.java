package net.labyfy.component.gamesettings.settings;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DisplayName;

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
