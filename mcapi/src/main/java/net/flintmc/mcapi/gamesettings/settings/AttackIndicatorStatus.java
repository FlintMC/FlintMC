package net.flintmc.mcapi.gamesettings.settings;

/** An enumeration representing attack indicator statuses. */
public enum AttackIndicatorStatus {
  OFF("options.off"),
  CROSSHAIR("options.attack.crosshair"),
  HOTBAR("options.attack.hotbar");

  private final String resourceKey;

  AttackIndicatorStatus(String resourceKey) {
    this.resourceKey = resourceKey;
  }

  public String getResourceKey() {
    return resourceKey;
  }
}
