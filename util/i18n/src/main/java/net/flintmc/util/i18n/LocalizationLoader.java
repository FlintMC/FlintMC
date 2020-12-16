package net.flintmc.util.i18n;

/**
 * Represents a loader for localizations.
 */
public interface LocalizationLoader {

  /**
   * Loads all package translations.
   *
   * @param localization The localization to add translations.
   * @param languageCode The language code to load the correct translations.
   */
  void load(Localization localization, String languageCode);
}
