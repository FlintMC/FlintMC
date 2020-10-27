package net.flintmc.mcapi.gamesettings.settings;

/**
 * An enumeration representing options for the graphics.
 */
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
