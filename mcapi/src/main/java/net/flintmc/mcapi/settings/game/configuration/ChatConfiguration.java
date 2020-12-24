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

package net.flintmc.mcapi.settings.game.configuration;

import net.flintmc.framework.config.annotation.implemented.ImplementedConfig;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ui.DefineCategory;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.options.BooleanSetting;
import net.flintmc.mcapi.settings.flint.options.dropdown.EnumSelectSetting;
import net.flintmc.mcapi.settings.flint.options.numeric.Range;
import net.flintmc.mcapi.settings.flint.options.numeric.SliderSetting;
import net.flintmc.mcapi.settings.game.settings.ChatVisibility;
import net.flintmc.mcapi.settings.game.settings.NarratorStatus;

/**
 * Represents the chat configuration.
 */
@DefineCategory(
    name = "minecraft.settings.chat",
    displayName = @Component(value = "options.chat.title", translate = true))
@ImplementedConfig
public interface ChatConfiguration {

  /**
   * Retrieves the scale of the chat.
   *
   * @return The chat scale.
   */
  // percent
  @SliderSetting(value = @Range(max = 100))
  @DisplayName(@Component(value = "options.chat.scale", translate = true))
  double getChatScale();

  /**
   * Changes the scale of the chat.
   *
   * @param chatScale The new scale of the chat.
   */
  void setChatScale(double chatScale);

  /**
   * Retrieves the width of the chat.
   *
   * @return The chat width.
   */
  // pixels
  @SliderSetting(value = @Range(min = 40, max = 320))
  @DisplayName(@Component(value = "options.chat.width", translate = true))
  double getChatWidth();

  /**
   * Changes the width of the chat.
   *
   * @param chatWidth The new chat width.
   */
  void setChatWidth(double chatWidth);

  /**
   * Retrieves the max height that the chat is allowed to appear normally.
   *
   * @return The max height that the chat is allowed to appear normally.
   */
  // pixels
  @SliderSetting(value = @Range(min = 20, max = 180))
  @DisplayName(@Component(value = "options.chat.height.unfocused", translate = true))
  double getChatHeightUnfocused();

  /**
   * Changes the max height that the chat is allowed to appear normally.
   *
   * @param chatHeightUnfocused The new unfocused chat height.
   */
  void setChatHeightUnfocused(double chatHeightUnfocused);

  /**
   * Retrieves the max height that the chat is allowed to appear when in focus.
   *
   * @return The max height that the chat is allowed to appear when in focus.
   */
  // pixels
  @SliderSetting(value = @Range(min = 20, max = 180))
  @DisplayName(@Component(value = "options.chat.height.focused", translate = true))
  double getChatHeightFocused();

  /**
   * Changes the max height that the chat is allowed to appear when in focus.
   *
   * @param chatHeightFocused The new focused chat height.
   */
  void setChatHeightFocused(double chatHeightFocused);

  /**
   * Retrieves the delay of chat messages.
   *
   * @return The delay of chat messages.
   */
  double getChatDelay();

  /**
   * Changes the chat delay of messages.
   *
   * @param chatDelay The new chat delay.
   */
  void setChatDelay(double chatDelay);

  /**
   * Whether colors are displayed in the chat.
   *
   * @return {@code true} if colors are displayed in the chat, otherwise {@code false}.
   */
  @BooleanSetting
  @DisplayName(@Component(value = "options.chat.color", translate = true))
  boolean isChatColor();

  /**
   * Changes whether colors are displayed in the chat.
   *
   * @param chatColor The new state.
   */
  void setChatColor(boolean chatColor);

  /**
   * Whether links can be seen in the chat.
   *
   * @return {@code true} if links can be seen in the chat, otherwise {@code false}.
   */
  @BooleanSetting
  @DisplayName(@Component(value = "options.chat.links", translate = true))
  boolean isChatLinks();

  /**
   * Changes whether links can be seen in the chat.
   *
   * @param chatLinks The new state.
   */
  void setChatLinks(boolean chatLinks);

  /**
   * Whether each time a player clicks on a URL, a prompt is forced to appear on the player's screen
   * to ensure that they want to leave Minecraft and go to the website.
   *
   * @return {@code true} if forces a prompt, otherwise {@code false}.
   */
  @BooleanSetting
  @DisplayName(@Component(value = "options.chat.links.prompt", translate = true))
  boolean isChatLinksPrompt();

  /**
   * Changes whether to force a prompt on the player screen each time a player clicks a URL to
   * ensure that they want to leave Minecraft and go to the website.
   *
   * @param chatLinksPrompt The new state for the links prompt.
   */
  void setChatLinksPrompt(boolean chatLinksPrompt);

  /**
   * Retrieves the visibility of the chat.
   *
   * @return The chat visibility.
   */
  @EnumSelectSetting
  @DisplayName(@Component(value = "options.chat.visibility", translate = true))
  ChatVisibility getChatVisibility();

  /**
   * Changes the visibility of the chat.
   *
   * @param chatVisibility The new chat visibility.
   */
  void setChatVisibility(ChatVisibility chatVisibility);

  /**
   * Retrieves the opacity of the chat.
   *
   * @return The chat opacity.
   */
  // percent
  @SliderSetting(value = @Range(min = 10, max = 100))
  @DisplayName(@Component(value = "options.chat.opacity", translate = true))
  double getChatOpacity();

  /**
   * Changes the opacity of the chat.
   *
   * @param chatOpacity The new chat opacity.
   */
  void setChatOpacity(double chatOpacity);

  /**
   * Retrieves the line spacing between texts.
   *
   * @return The line spacing between texts.
   */
  double getChatLineSpacing();

  /**
   * Changes the chat line spacing between texts.
   *
   * @param chatLineSpacing The new chat line spacing.
   */
  void setChatLineSpacing(double chatLineSpacing);

  /**
   * Retrieves the narrator status.
   *
   * @return The current narrator status.
   */
  @EnumSelectSetting
  @DisplayName(@Component(value = "options.narrator", translate = true))
  NarratorStatus getNarrator();

  /**
   * Changes the narrator status.
   *
   * @param narrator The new narrator status.
   */
  void setNarrator(NarratorStatus narrator);

  /**
   * Whether command suggestions show up or not, if off the player has to press tab to bring them
   * up.
   *
   * @return {@code true} if command suggestions show up, otherwise {@code false}.
   */
  @BooleanSetting
  @DisplayName(@Component(value = "options.autoSuggestCommands", translate = true))
  boolean isAutoSuggestCommands();

  /**
   * Changes whether the commands are suggested automatically.
   *
   * @param autoSuggestCommands The new state for auto suggest commands.
   */
  void setAutoSuggestCommands(boolean autoSuggestCommands);

  /**
   * Whether reduced information should be displayed on the debug screen.
   *
   * @return {@code true} if reduced information should be displayed, otherwise {@code false}.
   */
  @BooleanSetting
  @DisplayName(@Component(value = "options.reducedDebugInfo", translate = true))
  boolean isReducedDebugInfo();

  /**
   * Changes the reduced information.
   *
   * @param reducedDebugInfo The new reduced debug information.
   */
  void setReducedDebugInfo(boolean reducedDebugInfo);

  /**
   * Retrieves the opacity of the background.
   *
   * @return The opacity of the background.
   */
  // pixels
  @SliderSetting(value = @Range(max = 100))
  @DisplayName(
      @Component(value = "options.accessibility.text_background_opacity", translate = true))
  double getAccessibilityTextBackgroundOpacity();

  /**
   * Changes the opacity of the text background.
   *
   * @param accessibilityTextBackgroundOpacity The new text background opacity.
   */
  void setAccessibilityTextBackgroundOpacity(double accessibilityTextBackgroundOpacity);

  /**
   * Whether the text has a background.
   *
   * @return {@code true} if the text has a background, otherwise {@code false}.
   */
  boolean isAccessibilityTextBackground();

  /**
   * Changes whether the text has a background.
   *
   * @param accessibilityTextBackground The new background state.
   */
  void setAccessibilityTextBackground(boolean accessibilityTextBackground);
}
