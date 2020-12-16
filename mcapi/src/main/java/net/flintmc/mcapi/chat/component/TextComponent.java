package net.flintmc.mcapi.chat.component;

/**
 * A component for displaying normal text in the chat.
 */
public interface TextComponent extends ChatComponent {

  /**
   * Retrieves the raw text of this component which will be displayed by the client or {@code null}
   * if no text has been specified.
   *
   * @return The text or {@code null} if no text has been provided
   * @see #text(String)
   */
  String text();

  /**
   * Sets the text of this component which will be displayed by the client.
   *
   * @param text The new non-null text for this component
   */
  void text(String text);
}
