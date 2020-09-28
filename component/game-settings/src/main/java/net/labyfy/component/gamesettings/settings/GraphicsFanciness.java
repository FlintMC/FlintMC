package net.labyfy.component.gamesettings.settings;

public enum GraphicsFanciness {

  FAST("options.graphics.fast"),
  FANCY("options.graphics.fancy"),
  FABULOUS("options.graphics.fabulous");

  private final String resourceKey;

  GraphicsFanciness(String resourceKey) {
    this.resourceKey = resourceKey;
  }

  public String getResourceKey() {
    return resourceKey;
  }
}
