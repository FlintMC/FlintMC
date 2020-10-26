package net.labyfy.component.i18n;

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

}
