package net.flintmc.util.i18n;

import java.util.Collection;

/**
 * Represents Minecraft's I18n.
 */
public interface I18n {

  /**
   * Translates the {@code translationKey} to a `human readable` message.
   *
   * @param translationKey The translation key.
   * @param parameters     The parameters for the translations.
   * @return A translated human readable message.
   */
  String translate(String translationKey, Object... parameters);

  /**
   * Whether the translation is exists in the selected language.
   *
   * @param key The key to be checked.
   * @return {@code true} if the given is exists the selected language, otherwise {@code false}.
   */
  boolean hasTranslation(String key);

  /**
   * Retrieves a collection of all available languages in the format "Name (Region)"..
   *
   * @return A non-null collection of all available languages, modification to it won't have any
   * effect
   */
  Collection<String> getAvailableLanguages();

}
