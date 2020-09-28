package net.labyfy.component.gamesettings.settings;

public enum NarratorStatus {

  OFF("options.narrator.off"),
  ALL("options.narrator.all"),
  CHAT("options.narrator.chat"),
  SYSTEM("options.narrator.system");

  private final String resourceKey;

  NarratorStatus(String resourceKey) {
    this.resourceKey = resourceKey;
  }

  public String getResourceKey() {
    return resourceKey;
  }
}
