package net.labyfy.component.gamesettings.settings;

public enum ParticleStatus {

  ALL("options.particles.all"),
  DECREASED("options.particles.decreased"),
  MINIMAL("options.particles.minimal");

  private final String resourceKey;

  ParticleStatus(String resourceKey) {
    this.resourceKey = resourceKey;
  }

  public String getResourceKey() {
    return resourceKey;
  }
}
