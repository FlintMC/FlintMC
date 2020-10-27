package net.flintmc.mcapi.player.overlay;

import net.flintmc.mcapi.chat.component.ChatComponent;

/** Represents the tab overlay */
public interface TabOverlay {

  /**
   * Retrieves the header of this player.
   *
   * @return The header of this player
   */
  ChatComponent getHeader();

  /**
   * Updates the header of this player.
   *
   * @param header The new header content
   */
  void updateHeader(ChatComponent header);

  /**
   * Retrieves the footer of this player.
   *
   * @return The footer of this player.
   */
  ChatComponent getFooter();

  /**
   * Updates the footer of this player.
   *
   * @param footer The new footer content
   */
  void updateFooter(ChatComponent footer);
}
