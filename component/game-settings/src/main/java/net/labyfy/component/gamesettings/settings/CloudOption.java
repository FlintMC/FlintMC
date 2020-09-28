package net.labyfy.component.gamesettings.settings;

public enum CloudOption {

  OFF("options.off"),
  FAST("options.clouds.fast"),
  FANCY("options.clouds.fancy");

  private final String translationKey;

  CloudOption(String translationKey) {
    this.translationKey = translationKey;
  }

  public String getTranslationKey() {
    return translationKey;
  }
}
