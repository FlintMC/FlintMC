package net.flintmc.mcapi.gamesettings.settings;

/** An enumeration representing options for clouds. */
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
