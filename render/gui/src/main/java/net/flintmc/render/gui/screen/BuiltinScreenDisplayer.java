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

package net.flintmc.render.gui.screen;

import net.flintmc.render.gui.event.KeyEvent;

/**
 * Interface abstracting the displaying of Minecraft GUI screens
 */
public interface BuiltinScreenDisplayer {

  /**
   * Tests whether this screen displayer is capable of displaying the given screen
   *
   * @param screenName The name of the screen to display
   * @return {@code true} if the displayer supports the screen, {@code false} otherwise
   */
  boolean supports(ScreenName screenName);

  /**
   * Changes the currently active GUI screen to the given one. This method may only be called with a
   * screen supported. To test for support, use the {@link #supports(ScreenName)} method.
   *
   * @param screenName The name of the screen to display or {@code null} to display no screen
   * @param args       Parameters to pass to the screen
   * @throws UnsupportedOperationException If the screen given is not supported by this displayer
   * @throws IllegalArgumentException      If the arguments given are not acceptable for the given
   *                                       screen
   */
  void display(ScreenName screenName, Object... args);

  /**
   * Retrieves whether there is an active GUI screen. An open screen for example is the chat and the
   * main menu, it is not the ingame screen itself without any other GUI.
   *
   * @return {@code true} if there is a screen opened (e.g. the chat, inventory), {@code false}
   * otherwise
   * @see #getOpenScreen()
   */
  boolean isScreenOpened();

  /**
   * Retrieves the currently active GUI screen, this will never return a screen name of the type
   * {@link ScreenName.Type#FROM_MINECRAFT} with one exception being {@link ScreenName#DUMMY} and
   * may not be {@link #supports(ScreenName) supported by this displayer}. Those screens for example
   * can be the chat, the main menu, etc.
   *
   * @return The currently open screen or {@code null} if there is currently no screen opened (e.g.
   * the ingame screen without the chat opened)
   */
  ScreenName getOpenScreen();

  /**
   * Displays a dummy screen in Minecraft so that Minecraft will render the mouse like it would be
   * done when for example opening the chat or an inventory. If there is already another screen like
   * this opened, nothing will happen. To reset this again display another screen or {@code null}
   * with {@link #display(ScreenName, Object...)}. Pressing Escape with the {@link KeyEvent} not
   * being cancelled will also close this dummy and therefore disable that the mouse is being
   * displayed.
   * <p>
   * The {@link ScreenName} by {@link #getOpenScreen()} will be {@link ScreenName#DUMMY}.
   */
  void displayMouse();
}
