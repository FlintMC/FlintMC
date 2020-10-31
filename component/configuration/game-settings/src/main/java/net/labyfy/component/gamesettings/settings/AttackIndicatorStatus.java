package net.labyfy.component.gamesettings.settings;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DisplayName;

/**
 * An enumeration representing attack indicator statuses.
 */
public enum AttackIndicatorStatus {

  // TODO remove all 'resourceKey' values (also from the other enums in this class)

  @DisplayName(@Component(value = "options.off", translate = true))
  OFF("options.off"),

  @DisplayName(@Component(value = "options.attack.crosshair", translate = true))
  CROSSHAIR("options.attack.crosshair"),

  @DisplayName(@Component(value = "options.attack.hotbar", translate = true))
  HOTBAR("options.attack.hotbar");

  private final String resourceKey;

  AttackIndicatorStatus(String resourceKey) {
    this.resourceKey = resourceKey;
  }

  public String getResourceKey() {
    return resourceKey;
  }
}
