package net.labyfy.chat.component.event.content;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.event.HoverEvent;

/**
 * The content of a {@link HoverEvent} which displays a {@link ChatComponent}.
 */
public class HoverText extends HoverContent {

  private final ChatComponent text;

  /**
   * Creates a new content for a {@link HoverEvent} which displays a text.
   *
   * @param text The non-null component for the content
   */
  public HoverText(ChatComponent text) {
    this.text = text;
  }

  /**
   * Retrieves the non-null component of this text which is used when displaying the text.
   *
   * @return The non-null component of this text.
   */
  public ChatComponent getText() {
    return this.text;
  }

  @Override
  public HoverEvent.Action getAction() {
    return HoverEvent.Action.SHOW_TEXT;
  }
}
