package net.flintmc.util.i18n;

import java.util.Map;

/**
 * Represents the localization.
 */
public interface Localization {

  /**
   * Adds a new translation to the key-value system.
   *
   * @param key         The key for the translation.
   * @param translation The human readable translation.
   */
  void add(String key, String translation);

  /**
   * Retrieves a key-value system with all available translations.
   *
   * @return A key-value system with all available translations.
   */
  Map<String, String> getProperties();
}
