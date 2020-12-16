package net.flintmc.mcapi.chat.component;

/**
 * A component for displaying the text of a translation in the language selected by the client.
 */
public interface TranslationComponent extends ChatComponent {

  /**
   * Retrieves the key for translations of this component or {@code null} if no key has been set.
   *
   * @return The key for translations or {@code null} if no translation key has been provided
   * @see #translationKey(String)
   */
  String translationKey();

  /**
   * Sets the key for translations of this component.
   *
   * @param translationKey The non-null key for translations
   */
  void translationKey(String translationKey);

  /**
   * Retrieves the translation value for the {@link #translationKey()} of this component or the key
   * if there is no translation available.
   *
   * @return The non-null result of the translation
   */
  String translate();

  /**
   * Sets the arguments for the translation of this component.
   *
   * <p>Minecraft will replace %s in the translation with the arguments of the {@link
   * TranslationComponent}.
   *
   * @param arguments The non-null arguments to pass to the client
   */
  void arguments(ChatComponent... arguments);

  /**
   * Appends a new argument to the arguments of this component for translation of the current
   * component.
   *
   * <p>Minecraft will replace %s in the translation with the arguments of the {@link
   * TranslationComponent}.
   *
   * @param argument The non-null argument to pass to the client
   */
  void appendArgument(ChatComponent argument);

  /**
   * Retrieves all arguments of this component that are used by Minecraft for the translation of the
   * {@link TranslationComponent}.
   *
   * @return A non-null array of all arguments. Modifications to this array will have no effect on
   * this component, however modifications to the components in this array will have an effect on
   * this component
   */
  ChatComponent[] arguments();
}
