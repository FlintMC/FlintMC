package net.flintmc.mcapi.chat;

import net.flintmc.mcapi.settings.game.settings.ChatVisibility;

/**
 * A location in the chat where components can be displayed by the {@link ChatController}.
 */
public enum ChatLocation {

  /**
   * The default chat.
   */
  CHAT,

  /**
   * The action bar above the hotbar.
   */
  ACTION_BAR,

  /**
   * The default chat but only if the user has enabled {@link ChatVisibility#SYSTEM}.
   */
  SYSTEM

}
