package net.flintmc.mcapi.chat.builder;

import net.flintmc.mcapi.chat.component.TextComponent;

/**
 * Builder for {@link TextComponent}s.
 */
public interface TextComponentBuilder extends ComponentBuilder<TextComponentBuilder> {

  /**
   * Sets the text of the current component. Every component can only have one text, so this
   * overrides any calls that have been done before to this method.
   *
   * @param text The new text of the component
   * @return this
   */
  TextComponentBuilder text(String text);

  /**
   * Appends a new text to the text of the current component.
   *
   * @param text The text to be appended
   * @return this
   */
  TextComponentBuilder appendText(String text);

  /**
   * Retrieves the text of the current component or {@code null} if no text has been set.
   *
   * @return The text of the current component or {@code null} if no text has been provided
   * @see #text(String)
   * @see #appendText(String)
   */
  String text();
}
