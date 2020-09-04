package net.labyfy.chat.builder;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.TranslationComponent;

/**
 * Builder for {@link TranslationComponent}s.
 */
public interface TranslationComponentBuilder extends ComponentBuilder<TranslationComponentBuilder> {

  /**
   * Sets the key for translations of the current component. Every component can only have one key, so this overrides
   * any calls that have been done before to this method.
   *
   * @param translationKey The key for the translation
   * @return this
   */
  TranslationComponentBuilder translationKey(String translationKey);

  /**
   * Retrieves the key for translations of the current component or {@code null} if no key has been set.
   *
   * @return The key for translations of the current component or {@code null} if no translation key has been provided
   * @see #translationKey(String)
   */
  String translationKey();

  /**
   * Sets the arguments for the translation of the current component.
   * <p>
   * Minecraft will replace %s the translation with the arguments of the {@link TranslationComponent}.
   *
   * @param arguments The non-null arguments to pass to the client
   * @return this
   */
  TranslationComponentBuilder arguments(ChatComponent... arguments);

  /**
   * Appends a new argument to the arguments of the current component for the translation of the current component.
   * <p>
   * Minecraft will replace %s the translation with the arguments of the {@link TranslationComponent}.
   *
   * @param argument The non-null argument to pass to the client
   * @return this
   */
  TranslationComponentBuilder appendArgument(ChatComponent argument);

  /**
   * Retrieves all arguments of the current component which Minecraft uses for the translation of the {@link
   * TranslationComponent}.
   *
   * @return A non-null array of all arguments. Modifications to this array will have no effect on the current component
   */
  ChatComponent[] arguments();

}
