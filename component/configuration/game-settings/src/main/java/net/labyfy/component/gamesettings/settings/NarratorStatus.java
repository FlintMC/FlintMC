package net.labyfy.component.gamesettings.settings;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.settings.annotation.ui.DisplayName;

/**
 * An enumeration representing narrator statuses.
 */
public enum NarratorStatus {

  @DisplayName(@Component(value = "options.narrator.off", translate = true))
  OFF("options.narrator.off"),

  @DisplayName(@Component(value = "options.narrator.all", translate = true))
  ALL("options.narrator.all"),

  @DisplayName(@Component(value = "options.narrator.chat", translate = true))
  CHAT("options.narrator.chat"),

  @DisplayName(@Component(value = "options.narrator.system", translate = true))
  SYSTEM("options.narrator.system");

  private final String resourceKey;

  NarratorStatus(String resourceKey) {
    this.resourceKey = resourceKey;
  }

  public String getResourceKey() {
    return resourceKey;
  }
}
