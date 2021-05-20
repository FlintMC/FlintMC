/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.chat.component.event;

/**
 * The implementation of a click event for chat components that will be executed when the player
 * clicks on the component.
 *
 * <p>ClickEvents for the chat are available since Minecraft 1.7.10. With Minecraft 1.8 and 1.15
 * some new actions have been added.
 */
public interface ClickEvent {

  /**
   * Retrieves the non-null action of this click event.
   *
   * @return The non-null action of this click event
   */
  Action getAction();

  /**
   * Retrieves the non-null value of this click event.
   *
   * @return The non-null value of this click event
   */
  String getValue();

  /**
   * Runs this click event, for example if the {@link #getAction() action} is {@link
   * Action#COPY_TO_CLIPBOARD}, the {@link #getValue() value} will be copied into the clipboard.
   */
  void execute();

  /**
   * Factory for the {@link ClickEvent}.
   */
  interface Factory {

    /**
     * Creates a new click event with the given action and value.
     *
     * <p>Available since Minecraft 1.7.10.
     *
     * @param action The non-null action of the click event
     * @param value  The non-null value of the click event
     * @return The new non-null click event
     * @see Action
     */
    ClickEvent create(Action action, String value);

    /**
     * Creates a new click event which will open an URL in the Browser when the player clicks on
     * it.
     *
     * <p>Available since Minecraft 1.7.10.
     *
     * @param url The url to be opened
     * @return The new non-null click event
     */
    ClickEvent openUrl(String url);

    /**
     * Creates a new click event which will open a file on the local file system. For security
     * reasons, this is only used internally to open screenshots in the chat and ignored when the
     * server sends it.
     *
     * <p>Available since Minecraft 1.7.10.
     *
     * @param path The path to the file to be opened
     * @return The new non-null click event
     */
    ClickEvent openFile(String path);

    /**
     * Creates a new click event which will send the given command as a normal chat message to the
     * server.
     *
     * <p>Available since Minecraft 1.7.10.
     *
     * @param command The command to be sent to the server
     * @return The new non-null click event
     */
    ClickEvent runCommand(String command);

    /**
     * Creates a new click event which will fill the chat input with given command.
     *
     * <p>Available since Minecraft 1.7.10.
     *
     * @param command The command for the chat
     * @return The new non-null click event
     */
    ClickEvent suggestCommand(String command);

    /**
     * Creates a new click event which will copy the given content to the clipboard of the user.
     *
     * <p>Available since Minecraft 1.15.
     *
     * @param content The content for the clipboard
     * @return The new non-null click event
     */
    ClickEvent copyToClipboard(String content);

    /**
     * Creates a new click event which will change the page in the currently opened book, this event
     * has no effect when used somewhere else than a book.
     *
     * <p>Available since Minecraft 1.8.
     *
     * @param page The number of the page to be opened
     * @return The new non-null click event
     */
    ClickEvent changeBookPage(int page);

  }

  /**
   * An enumeration representing all possible actions for a {@link ClickEvent}.
   */
  enum Action {

    /**
     * Opens an URL in the Browser of the player.
     *
     * <p>Available since Minecraft 1.7.10.
     */
    OPEN_URL,

    /**
     * Opens a file on the local file system of the player. For security reasons, this is only used
     * internally and will be ignored if sent by servers.
     *
     * <p>Available since Minecraft 1.7.10.
     */
    OPEN_FILE,

    /**
     * Sends a chat message with the given value.
     *
     * <p>Available since Minecraft 1.7.10.
     */
    RUN_COMMAND,

    /**
     * Fills contents of the chat with the given value.
     *
     * <p>Available since Minecraft 1.7.10.
     */
    SUGGEST_COMMAND,

    /**
     * This action is only available in books, it changes the page of the book to the page defined
     * in the value.
     *
     * <p>Available since Minecraft 1.8
     */
    CHANGE_PAGE,

    /**
     * Copies the given value into the clipboard of the player.
     *
     * <p>Available since Minecraft 1.15
     */
    COPY_TO_CLIPBOARD;

    private final String lowerName;

    Action() {
      this.lowerName = super.name().toLowerCase();
    }

    /**
     * Retrieves the {@link #name()} of this enum constant in the lower case.
     *
     * @return The non-null lower case name of this enum constant
     */
    public String getLowerName() {
      return this.lowerName;
    }
  }
}
